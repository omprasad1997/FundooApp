package com.example.loginapp.data.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.loginapp.models.Note
import com.google.firebase.ktx.Firebase

class DatabaseHandler(context: Context) : SQLiteOpenHelper(
    context,
    DBConstants.DATABASE_NAME, null,
    DBConstants.DATABASE_VERSION
) {

    companion object {

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USER_TABLE = ("CREATE TABLE " + DBConstants.UsersTable.TABLE_USERS + "("
                + DBConstants.UsersTable.KEY_ID + " INTEGER PRIMARY KEY," + DBConstants.UsersTable.FOREIGN_KEY +  + TABLE_NOTE_LIST)

        val CREATE_NOTE_LIST = ("CREATE TABLE " + TABLE_NOTE_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NOTE + " TEXT,"
                + KEY_TITLE + " TEXT" + ")")
        db?.execSQL(CREATE_USER_TABLE)
        db?.execSQL(CREATE_NOTE_LIST)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NOTE_LIST")
        onCreate(db)
    }

}
