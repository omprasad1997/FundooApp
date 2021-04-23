package com.example.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var logout:Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        logout = findViewById(R.id.logout)
        mAuth = FirebaseAuth.getInstance()
    }

    fun userLogout(view: View) {
        mAuth.signOut()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}