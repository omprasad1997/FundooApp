package com.example.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var userEmail:EditText
    private lateinit var userPassword:EditText
    private lateinit var register:Button
    private lateinit var login:Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Login"

        userEmail    = findViewById(R.id.userEmail)
        userPassword = findViewById(R.id.userPassword)
        register = findViewById(R.id.userRegister)
        login    = findViewById(R.id.login)
        mAuth = FirebaseAuth.getInstance()
    }

    fun userLogin(view: View) {
        val email = userEmail.text.toString()
        val password = userPassword.text.toString()

        if(email.isEmpty() || !email.contains("@")) {
            userEmail.error = "Your Email is not valid"
        } else if(password.isEmpty() || password.length < 7) {
            userPassword.error = "Password must be 7 characters"
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {result ->
                Log.e("validation","Successful Login")
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)

            }.addOnFailureListener{
                Log.e("validation","Failed Login", it)
            }
        }
    }

    fun userRegistration(view: View) {
        val intent = Intent(this,SignupForm::class.java)
        startActivity(intent)
    }
}