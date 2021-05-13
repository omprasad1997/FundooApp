package com.example.loginapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentTransaction
import com.example.loginapp.R
import com.example.loginapp.models.FirebaseNoteDataManager
import com.google.android.material.snackbar.Snackbar


class CreateNoteFragment() : Fragment() {
    private lateinit var writtenNote: EditText
    private lateinit var noteTitle: EditText
    private lateinit var saveNoteButton: Button
    private lateinit var firebaseNoteDataManager: FirebaseNoteDataManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_note, container, false)
        initializationOfViews(view)
        return view
    }

    private fun initializationOfViews(view: View?) {
        if (view != null) {
            writtenNote = view.findViewById(R.id.writtenNote)
            noteTitle = view.findViewById(R.id.noteTitle)
            saveNoteButton = view.findViewById(R.id.saveNoteButton)
            firebaseNoteDataManager = FirebaseNoteDataManager()
        }
        saveNoteButton.setOnClickListener {
            val title = noteTitle.text.toString()
            val note = writtenNote.text.toString()

            if (note.isEmpty() || title.isEmpty()) {
                Snackbar.make(it, "Note or title field is empty", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
            } else {
                firebaseNoteDataManager.saveDataToFireStoreWithSubCollection(title, note, view)
            }
        }
    }
}