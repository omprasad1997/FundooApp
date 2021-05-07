package com.example.loginapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.loginapp.R
import com.example.loginapp.Class.SharedPreferenceHelper

class SplashScreenActivity : AppCompatActivity() {
    private var TIME_OUT:Long = 3000
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sharedPreferenceHelper = SharedPreferenceHelper(this)
        loadSplashScreen()
    }

    private fun loadSplashScreen() {
        Handler().postDelayed({
            // You can declare your desire activity here to open after finishing splash screen. Like MainActivity
            val checkLogin = sharedPreferenceHelper.getLoggedIn()

            if(!checkLogin) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else {
                val intent = Intent(this, HomeDashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
        },TIME_OUT)
    }
}