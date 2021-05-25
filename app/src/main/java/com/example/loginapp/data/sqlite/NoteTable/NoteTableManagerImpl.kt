package com.example.loginapp.data.sqlite.NoteTable

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.example.loginapp.Firebase.DataManager.Model.FirebaseUserModel
import com.example.loginapp.Firebase.DataManager.Note
import com.example.loginapp.data.sqlite.DBConstants
import com.example.loginapp.data.sqlite.DatabaseHandler

class NoteTableManagerImpl(private val databaseHandler: DatabaseHandler) :
    NoteTableManager {

    override fun addNote(note: Note): Boolean {
        val db = databaseHandler.writableDatabase
        val notesValues = ContentValues()
        notesValues.put(DBConstants.NotesTable.DOCUMENT_REFERENCE_ID, note.id)
        notesValues.put(DBConstants.NotesTable.KEY_TITLE, note.title)
        notesValues.put(DBConstants.NotesTable.KEY_NOTE, note.notes)
        notesValues.put(DBConstants.NotesTable.FOREIGN_KEY, note.userUid)
        // Inserting Row
        val success = db.insert(DBConstants.NotesTable.NOTE_LIST_TABLE, null, notesValues)

        db.close()
        return success > 0
    }

    override fun getAllNotes(): List<Note> {
        val noteList: ArrayList<Note> = ArrayList<Note>()
        val selectQuery = "SELECT  * FROM ${DBConstants.NotesTable.NOTE_LIST_TABLE}"
        val db = databaseHandler.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        var userId: Int
        var title: String
        var note: String
        var userUid: String
        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("id"))
                title = cursor.getString(cursor.getColumnIndex("title"))
                note = cursor.getString(cursor.getColumnIndex("note"))
                userUid = cursor.getString(cursor.getColumnIndex("userUid"))
                val note = Note(
                    userId.toString(),
                    title,
                    note,
                    userUid
                )
                noteList.add(note)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return noteList
    }

    override fun deleteNote(noteId: String): Boolean {
        val db = databaseHandler.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBConstants.NotesTable.DOCUMENT_REFERENCE_ID, noteId)
        // Deleting Row
        val success = db.delete(DBConstants.NotesTable.NOTE_LIST_TABLE, "id=$noteId", null)

        db.close()
        return success > 0
    }

    override fun deleteAllNotes() {
        val db = databaseHandler.writableDatabase
        db.execSQL("DELETE FROM ${DBConstants.NotesTable.NOTE_LIST_TABLE}")
        db.close()
    }

    override fun addAllNotes(notes: ArrayList<Note>) {
        notes.forEach {
            addNote(it)
        }
    }
}