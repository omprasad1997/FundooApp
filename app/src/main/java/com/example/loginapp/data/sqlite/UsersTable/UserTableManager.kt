package com.example.loginapp.data.sqlite.UsersTable

import com.example.loginapp.Firebase.DataManager.Model.FirebaseUserModel

interface UserTableManager {

    fun addUser(firebaseUserModel: FirebaseUserModel)
}

