package com.example.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {
    private lateinit var userEmail:EditText
    private lateinit var userPassword:EditText
    private lateinit var login:Button
    private lateinit var register:TextView
    private lateinit var resetPassword:TextView
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Login"

        userEmail    = findViewById(R.id.userEmail)
        userPassword = findViewById(R.id.userPassword)
        resetPassword = findViewById(R.id.resetPassword)
        register     = findViewById(R.id.userRegister)
        login        = findViewById(R.id.login)
        mAuth        = FirebaseAuth.getInstance()
    }

    fun userLogin(view: View) {
        val email = userEmail.text.toString()
        val password = userPassword.text.toString()

        if(email.isEmpty() || !email.contains("@")) {
            userEmail.error = "Your Email is not valid"
        } else if(password.isEmpty() || password.length < 7) {
            userPassword.error = "Password is incorrect"
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {result ->
                Log.e("validation","Successful Login")
                Toast.makeText(this, "Successfully Login ", Toast.LENGTH_SHORT).show()
                finish()
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener {
                Log.e("validation","Failed Login", it)
                when(it) {
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

    fun userRegistration(view: View) {
        finish()
        val intent = Intent(this,SignupActivity::class.java)
        startActivity(intent)

    }

    fun resetPassword(view: View) {
        val intent = Intent(this,ForgotPasswordActivity::class.java)
        startActivity(intent)
    }
}