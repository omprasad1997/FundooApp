package com.example.loginapp.data.sqlite.UsersTable

import android.content.ContentValues
import com.example.loginapp.Firebase.DataManager.Model.FirebaseUserModel
import com.example.loginapp.data.sqlite.DBConstants
import com.example.loginapp.data.sqlite.DatabaseHandler

class UserTableManagerImpl(private val databaseHandler: DatabaseHandler) : UserTableManager {
    override fun addUser(firebaseUserModel: FirebaseUserModel) {
        val db = databaseHandler.writableDatabase
        val userDetails = ContentValues()
        userDetails.put(DBConstants.UsersTable.USERS_UID, firebaseUserModel.userUid)
        userDetails.put(DBConstants.UsersTable.USER_NAME, firebaseUserModel.userName)
        userDetails.put(DBConstants.UsersTable.USER_EMAIL, firebaseUserModel.userEmail)

        // Inserting Row
        db.insert(DBConstants.UsersTable.TABLE_USERS, null, userDetails)
        db.close()
    }
}