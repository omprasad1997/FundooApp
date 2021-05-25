package com.example.loginapp.data.sqlite.NoteTable

import com.example.loginapp.Firebase.DataManager.Note
import java.util.ArrayList

interface NoteTableManager {

    fun addNote(note: Note): Boolean
    fun getAllNotes(): List<Note>
    fun deleteNote(noteId: String):Boolean
    fun deleteAllNotes()
    fun addAllNotes(notes: ArrayList<Note>)
}