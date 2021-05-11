package com.example.loginapp.models

import android.content.Context
import android.content.SharedPreferences
import com.example.loginapp.Constants.Constants

class SharedPreferenceHelper(context: Context) {
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(
            Constants.FUNDOO_PREF_FILE_NAME, Context.MODE_PRIVATE)

    fun setLoggedIn(value: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(Constants.KEY, value)
            apply()
        }
    }

    fun getLoggedIn() = sharedPreferences.getBoolean(Constants.KEY, false)

}