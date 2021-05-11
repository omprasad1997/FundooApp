package com.example.loginapp.models

import android.content.Intent
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.loginapp.Activity.HomeDashboardActivity
import com.example.loginapp.Activity.LoginActivity
import com.example.loginapp.Activity.SignupActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


class FirebaseUserManager(val context: LoginActivity) {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    private fun isEmailValid(email: String): Boolean = email.isEmpty() || !email.contains("@gmail.com")
    private fun isPasswordValid(password: String): Boolean = password.isEmpty()

    fun login(userEmail: EditText, userPassword: EditText) {
        val email = userEmail.text.toString()
        val password = userPassword.text.toString()
        mAuth = FirebaseAuth.getInstance()
        sharedPreferenceHelper = SharedPreferenceHelper(context)
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
                    Toast.makeText(context, "Successfully Login ", Toast.LENGTH_SHORT).show()
                    context.finish()
                    this.sharedPreferenceHelper.setLoggedIn(true)
                    val intent = Intent(context, HomeDashboardActivity::class.java)
                    context.startActivity(intent)
                }.addOnFailureListener {
                    Log.e("validation", "Failed Login", it)
                    when (it) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            Toast.makeText(context, "Password is incorrect", Toast.LENGTH_SHORT).show()
                        }
                        is FirebaseAuthInvalidUserException -> {
                            Toast.makeText(context, "This Username is not exist ", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    fun registration() {
        val intent = Intent(context, SignupActivity::class.java)
        context.startActivity(intent)
    }
}