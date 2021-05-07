package com.example.loginapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.loginapp.Activity.LoginActivity
import com.example.loginapp.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
//    private lateinit var resetPasswordToolbar: Toolbar
    private lateinit var reset: Button
    private lateinit var emailForReset: EditText
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        setSupportActionBar(findViewById(R.id.resetPasswordToolbar))
        supportActionBar?.title = "Forgot Password"

        reset = findViewById(R.id.reset)
        emailForReset  = findViewById(R.id.emailForReset)
        mAuth = FirebaseAuth.getInstance()
    }

    fun resetUserPassword(view: View) {
        val email = emailForReset.text.toString()

        if(email.isEmpty()) {
            Toast.makeText(this, "Please enter email id", Toast.LENGTH_LONG).show()
        } else {
            mAuth.sendPasswordResetEmail(email).addOnSuccessListener {
                Log.e("Validation","Successfully reset link sent email")
                Toast.makeText(this,"Reset link sent to your email",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(this,"Please enter correct email id",Toast.LENGTH_SHORT).show()
                Log.e("Validation","Unable to sent reset link to email",it)
            }
        }
    }
}