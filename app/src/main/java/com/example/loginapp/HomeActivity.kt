package com.example.loginapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var logout:Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var sharedPreferenceHelper  : SharedPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        initializationOfViews()
    }

   fun initializationOfViews() {
        sharedPreferenceHelper = SharedPreferenceHelper(this)
        logout = findViewById(R.id.logout)
        mAuth = FirebaseAuth.getInstance()
    }

    fun userLogout(view: View) {
        mAuth.signOut()
        Toast.makeText(this, "Successfully logout ", Toast.LENGTH_LONG).show()
        this.sharedPreferenceHelper.setLoggedIn(false)
        finish()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}