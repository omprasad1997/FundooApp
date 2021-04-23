package com.example.loginapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth

class SignupForm : AppCompatActivity() {
    private lateinit var enteredFullName:EditText
    private lateinit var enteredUserName:EditText
    private lateinit var enteredEmail:EditText
    private lateinit var enteredPassword:EditText
    private lateinit var enteredConfirmPassword:EditText
    private lateinit var register: Button

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup__form)
        supportActionBar?.title = "Signup"

        enteredFullName = findViewById(R.id.fullName)
        enteredUserName = findViewById(R.id.userName)
        enteredEmail = findViewById(R.id.email)
        enteredPassword = findViewById(R.id.password)
        enteredConfirmPassword = findViewById(R.id.confirmPassword)
        register = findViewById(R.id.register)
        mAuth = FirebaseAuth.getInstance()

    }

    private fun checkCredentials() {
        val fullName = enteredFullName.text.toString()
        val userName = enteredUserName.text.toString()
        val email = enteredEmail.text.toString()
        val password = enteredPassword.text.toString()
        val confirmPassword = enteredConfirmPassword.text.toString()

        if(fullName.isEmpty() || (fullName.length < 7)) {
            enteredFullName.error = "Your Full name is not valid"
        } else if(userName.isEmpty() || (userName.length < 7)) {
            enteredFullName.error = "Your Full name is not valid"
        } else if(email.isEmpty() || !email.contains("@")) {
            enteredEmail.error = "Email is not valid"
        } else if(password.isEmpty() || password.length < 7) {
            enteredPassword.error = "Password must be 7 characters"
        }  else if(confirmPassword.isEmpty() || !confirmPassword.contains(password)) {
            enteredConfirmPassword.error = "Password not match"
        }else {
            mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {result ->
                Log.e("validation","Successful registration")

            }.addOnFailureListener{
                Log.e("validation","Failed registration", it)
            }
        }
    }

    fun registerUser(view: View) {
        checkCredentials()
    }
}
