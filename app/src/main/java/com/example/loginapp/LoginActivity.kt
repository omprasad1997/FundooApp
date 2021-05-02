package com.example.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

class LoginActivity : AppCompatActivity() {
    private lateinit var userEmail:EditText
    private lateinit var userPassword:EditText
    private lateinit var login:Button
    private lateinit var register:TextView
    private lateinit var resetPassword:TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var googleSignInButton: SignInButton
    private lateinit var sharedPreferenceHelper : SharedPreferenceHelper
    private val RC_SIGN_IN: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Login"

        initializationOfViews()
        googleSignInButton.setOnClickListener{
            signInWithGoogle()
        }

    }

    private fun initializationOfViews() {
        userEmail    = findViewById(R.id.userEmail)
        userPassword = findViewById(R.id.userPassword)
        resetPassword = findViewById(R.id.resetPassword)
        register     = findViewById(R.id.userRegister)
        login        = findViewById(R.id.login)
        googleSignInButton = findViewById(R.id.googleSignInButton)
        mAuth        = FirebaseAuth.getInstance()
        sharedPreferenceHelper = SharedPreferenceHelper(this)
    }

    private fun isEmailValid(email:String) : Boolean = email.isEmpty() || !email.contains("@gmail.com")
    private fun isPasswordValid(password:String) : Boolean = password.isEmpty()

    fun userLogin(view: View) {
        val email = userEmail.text.toString()
        val password = userPassword.text.toString()
        when {
            isEmailValid(email) -> {
                userEmail.error = "Email is not valid"
            }
            isPasswordValid(password) -> {
                userPassword.error = "Password is incorrect"
            }
            else -> {
                mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener { result ->
                    Log.e("validation", "Successful Login")
                    Toast.makeText(this, "Successfully Login ", Toast.LENGTH_SHORT).show()
                    finish()
                    this.sharedPreferenceHelper.setLoggedIn(true)
                    val intent = Intent(this, HomeDashboardActivity::class.java)
                    startActivity(intent)
                }.addOnFailureListener {
                    Log.e("validation", "Failed Login", it)
                    when (it) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            Toast.makeText(this, "Password is incorrect", Toast.LENGTH_SHORT).show()
                        }
                        is FirebaseAuthInvalidUserException -> {
                            Toast.makeText(this, "This Username is not exist ", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    fun userRegistration(view: View) {
        val intent = Intent(this,SignupActivity::class.java)
        startActivity(intent)
    }

    fun resetPassword(view: View) {
        val intent = Intent(this,ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

         mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            d("handleRequest", e.toString())
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    this.sharedPreferenceHelper.setLoggedIn(true)
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        finish()
        val intent  = Intent(this,HomeDashboardActivity::class.java)
        startActivity(intent)
    }
}