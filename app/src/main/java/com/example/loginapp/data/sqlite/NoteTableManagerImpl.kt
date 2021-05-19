package com.example.loginapp.data.sqlite

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.example.loginapp.models.Note

class NoteTableManagerImpl(private val databaseHandler: DatabaseHandler) : NoteTableManager{


    override fun addNote(note: Note): Boolean {
        val db = databaseHandler.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBConstants.NotesTable.KEY_ID, note.id)
        contentValues.put(DBConstants.NotesTable.KEY_TITLE, note.title)
        contentValues.put(DBConstants.NotesTable.KEY_NOTE, note.notes)
        // Inserting Row
        val success = db.insert(DBConstants.NotesTable.TABLE_NOTE_LIST, null, contentValues)

        db.close()
        return success > 0
    }

    override fun getAllNotes(): List<Note> {
        val noteList: ArrayList<Note> = ArrayList<Note>()
        val selectQuery = "SELECT  * FROM ${DBConstants.NotesTable.TABLE_NOTE_LIST}"
        val db = databaseHandler.readableDatabase
        var cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        var userId: Int
        var title: String
        var note: String
        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("id"))
                title = cursor.getString(cursor.getColumnIndex("title"))
                note = cursor.getString(cursor.getColumnIndex("note"))
                val note = Note(
                    userId.toString(),
                    title,
                    note
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
        contentValues.put(DBConstants.NotesTable.KEY_ID, noteId)
        // Deleting Row
        val success = db.delete(DBConstants.NotesTable.TABLE_NOTE_LIST, "id=$noteId", null)

        db.close()
        return success > 0
    }

}