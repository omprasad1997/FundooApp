package com.example.loginapp.data.sqlite

object DBConstants{
    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "FundooNotes"

    const val KEY_EMAIL = "email"


    object UsersTable{
        const val TABLE_USERS = "Users"
        const val KEY_ID = "id"
        const val KEY_USER_NAME = "user name"

    }

    object NotesTable{
        const val KEY_NOTE = "note"
        const val FOREIGN_KEY = "userUid"
        const val KEY_ID = "id"
        const val KEY_TITLE = "title"
        const val TABLE_NOTE_LIST = "noteList"
    }
}