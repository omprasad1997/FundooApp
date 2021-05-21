package com.example.loginapp.data.sqlite

object DBConstants{
    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "FundooNotes"

    object UsersTable{
        const val TABLE_USERS = "Users"
        const val USERS_UID = "userUid"
        const val USER_NAME = "user name"
        const val USER_EMAIL = "email"
    }

    object NotesTable{
        const val NOTE_LIST_TABLE = "noteList"
        const val KEY_NOTE = "note"
        const val DOCUMENT_REFERENCE_ID = "id"
        const val KEY_TITLE = "title"
        const val FOREIGN_KEY = "userUid"

    }
}