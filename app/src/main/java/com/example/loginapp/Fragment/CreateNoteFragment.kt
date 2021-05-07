package com.example.loginapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.loginapp.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class CreateNoteFragment : Fragment() {
    private lateinit var writtenNote: EditText
    private lateinit var noteTitle: EditText
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
            noteTitle    = view.findViewById(R.id.noteTitle)
            saveNoteButton = view.findViewById(R.id.saveNoteButton)
        }
        saveNoteButton.setOnClickListener {
            val title = noteTitle.text.toString()
            val note = writtenNote.text.toString()

            if (note.isEmpty() || title.isEmpty()) {
                Snackbar.make(it, "Note or title field is empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
            } else {
                saveDataToFireStoreWithSubCollection(title,note,view)
            }
        }
    }

    private fun saveDataToFireStoreWithSubCollection(title: String, note: String, view: View?) {
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val notesInformation = mutableMapOf<String,Any>()
        val usersInformation = mutableMapOf<String,Any>()
        val userUID = mAuth.currentUser?.uid
        notesInformation["title"] = title
        notesInformation["note"]  = note
        usersInformation["Name"]  = mAuth.currentUser?.displayName.toString()
        usersInformation["email"] = mAuth.currentUser?.email.toString()
        db.collection("Users").document(userUID.toString()).set(usersInformation)

        db.collection("Users").document(userUID.toString())
                .collection("noteList").add(notesInformation)
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