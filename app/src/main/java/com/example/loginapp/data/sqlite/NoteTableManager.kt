package com.example.loginapp.data.sqlite

import com.example.loginapp.models.Note

interface NoteTableManager {

    fun addNote(note: Note): Boolean
    fun getAllNotes(): List<Note>
    fun deleteNote(noteId: String):Boolean

}