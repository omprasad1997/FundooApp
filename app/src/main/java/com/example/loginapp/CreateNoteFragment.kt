package com.example.loginapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class CreateNoteFragment : Fragment() {
    private lateinit var writtenNote: EditText
    private lateinit var saveNoteButton: Button
    private lateinit var db:FirebaseFirestore
    private lateinit var mAuth : FirebaseAuth

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
            saveNoteButton = view.findViewById(R.id.saveNoteButton)
        }
        saveNoteButton.setOnClickListener {
            val note = writtenNote.text.toString()
            if (note.isEmpty()) {
                Snackbar.make(it, "Note is empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
            } else {
                saveDataToFireStore(note,view)
            }
        }

    }

    private fun saveDataToFireStore(note: String, view: View?) {
        db = FirebaseFirestore.getInstance()
        val notesMap = mutableMapOf<String,Any>()
        notesMap["note"]  = note

        db.collection("Notes").add(notesMap)
            .addOnSuccessListener {
                if (view != null) {
                    Snackbar.make(view, "Note saved successfully", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
                }
            }
            .addOnFailureListener {
                if (view != null) {
                    Snackbar.make(view, "Failed to save note", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
                }
            }
         }
    }