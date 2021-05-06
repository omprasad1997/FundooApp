package com.example.loginapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.loginapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignupActivity : AppCompatActivity() {
    private lateinit var enteredFirstName:EditText
    private lateinit var enteredLastName:EditText
    private lateinit var enteredEmail:EditText
    private lateinit var enteredPassword:EditText
    private lateinit var enteredConfirmPassword:EditText
    private lateinit var register: Button
    private lateinit var alreadyRegistered: TextView
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup__form)
        supportActionBar?.title = "Signup"
        initializationOfViews()
    }

    private fun initializationOfViews() {
        enteredFirstName = findViewById(R.id.firstName)
        enteredLastName = findViewById(R.id.lastName)
        enteredEmail = findViewById(R.id.email)
        enteredPassword = findViewById(R.id.password)
        enteredConfirmPassword = findViewById(R.id.confirmPassword)
        register = findViewById(R.id.register)
        alreadyRegistered = findViewById(R.id.alreadyRegistered)
        mAuth = FirebaseAuth.getInstance()
    }

    private fun isFirstNameValid(firstName:String) : Boolean {
        if(firstName.isEmpty() || (firstName.length < 3)) {
            enteredFirstName.error = "Your Full name is not valid"
        }else{
            return true
        }
        return false
    }

    private fun isLastNameValid(lastName:String) : Boolean {
        if(lastName.isEmpty() || (lastName.length < 3)) {
            enteredLastName.error = "Your last name is not valid"
        }else{
            return true
        }
        return false
    }

    private fun isEmailValid(email:String) : Boolean {
        if(email.isEmpty() || !email.contains("@gmail.com")) {
            enteredEmail.error = "Email is not valid"
        }else{
            return true
        }
        return false
    }

    private fun isPasswordValid(password:String) : Boolean {
        if(password.isEmpty() || password.length < 7) {
            enteredPassword.error = "Password must be 7 characters"
        }else{
            return true
        }
        return false
    }

    private fun checkCredentials() {
        val firstName = enteredFirstName.text.toString()
        val lastName = enteredLastName.text.toString()
        val email = enteredEmail.text.toString()
        val password = enteredPassword.text.toString()
        val confirmPassword = enteredConfirmPassword.text.toString()

        if (!isFirstNameValid(firstName)) {
            return
        } else if (!isLastNameValid(lastName)) {
            return
        } else if (!isEmailValid(email)) {
            return
        } else if (!isPasswordValid(password)) {
            return
        } else if (confirmPassword.isEmpty() || !confirmPassword.contains(password)) {
            enteredConfirmPassword.error = "Password does not match"
        } else {
            Log.e("Email" , "$email : $password")
            mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { result ->
                Toast.makeText(this, "Successfully registered ", Toast.LENGTH_LONG).show()
                Log.e("validation", "Successful registration")
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener {
                Log.e("validation", "Failed registration", it)
                if (it is FirebaseAuthUserCollisionException) {
                    Toast.makeText(this, "This email already exist ", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun registerUser(view: View) {
        checkCredentials()
    }

    fun alreadyRegistered(view: View) {
        finish()
    }
}
