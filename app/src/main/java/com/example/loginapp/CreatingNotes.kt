package com.example.loginapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar

class CreatingNotes : AppCompatActivity() {
    private lateinit var createNoteToolbar: Toolbar
    private lateinit var writtenNotes: EditText
    private lateinit var saveNote: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creating_notes)

        createNoteToolbar = findViewById(R.id.createNoteToolbar)
        setSupportActionBar(createNoteToolbar)
        supportActionBar?.title = "Create Notes"
        initializationOfViews()
    }

    private fun initializationOfViews() {
        writtenNotes = findViewById(R.id.writtenNotes)
        saveNote = findViewById(R.id.saveNote)
        saveNote.setOnClickListener {
            val note = writtenNotes.text.toString()
            if (note.isEmpty()) {
                Snackbar.make(it, "Notes is empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
            } else {
                Snackbar.make(it, "Notes Saved Successfully", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
            }
        }
    }
}