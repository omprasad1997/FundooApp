package com.example.loginapp.data.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHandler private constructor(val context: Context) : SQLiteOpenHelper(
    context,
    DBConstants.DATABASE_NAME, null,
    DBConstants.DATABASE_VERSION
) {
    companion object {
        @Volatile
        private var INSTANCE: DatabaseHandler? = null

        fun getInstance(context: Context): DatabaseHandler =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DatabaseHandler(context).also {
                    INSTANCE = it
                }
            }
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USER_TABLE = ("CREATE TABLE " + DBConstants.UsersTable.TABLE_USERS + "("
                + DBConstants.UsersTable.USERS_UID + "TEXT PRIMARY KEY,"
                + DBConstants.UsersTable.USER_NAME + "TEXT,"
                + DBConstants.UsersTable.USER_EMAIL + "TEXT" + ")")

        val CREATE_NOTE_TABLE = ("CREATE TABLE " + DBConstants.NotesTable.NOTE_LIST_TABLE + "("
                + DBConstants.NotesTable.DOCUMENT_REFERENCE_ID + " TEXT PRIMARY KEY,"
                + DBConstants.NotesTable.KEY_TITLE + "TEXT,"
                + DBConstants.NotesTable.KEY_NOTE + "TEXT"
                + "FOREIGN KEY (" + DBConstants.UsersTable.USERS_UID + ")"
                + "REFERENCE (" + DBConstants.UsersTable.TABLE_USERS
                + DBConstants.UsersTable.USERS_UID + ")")
        db?.execSQL(CREATE_USER_TABLE)
        db?.execSQL(CREATE_NOTE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${DBConstants.UsersTable.TABLE_USERS}")
        db?.execSQL("DROP TABLE IF EXISTS ${DBConstants.NotesTable.NOTE_LIST_TABLE}")
        onCreate(db)
    }
}
