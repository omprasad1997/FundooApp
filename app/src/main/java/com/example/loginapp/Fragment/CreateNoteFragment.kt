package com.example.loginapp.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.loginapp.R
import com.example.loginapp.data.sqlite.DatabaseHandler
import com.example.loginapp.models.FirebaseNoteDataManager
import com.example.loginapp.models.Note
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class CreateNoteFragment() : Fragment() {
    private lateinit var writtenNote: EditText
    private lateinit var noteTitle: EditText
    private lateinit var saveNoteButton: ImageView
    private lateinit var firebaseNoteDataManager: FirebaseNoteDataManager
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
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
            mAuth = FirebaseAuth.getInstance()
            saveNoteButton.setOnClickListener {

                val title = noteTitle.text.toString()
                val note = writtenNote.text.toString()
                val userUid = mAuth.currentUser?.uid

                if (note.isEmpty() || title.isEmpty()) {
                    Snackbar.make(it, "Note or title field is empty", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
                } else {
                    firebaseNoteDataManager.saveDataToFireStoreWithSubCollection(title, note
                    ) { noteId: String?, exception: Exception? ->
                        noteId?.let {
                            db.addNote(Note(noteId,title, note))
                            Snackbar.make(view, "Note saved successfully", Snackbar.LENGTH_LONG)
                                .setAction("Action", null)
                                .show()
                        }

                        exception?.let {
                            Snackbar.make(view, "Failed to save note", Snackbar.LENGTH_LONG)
                                .setAction("Action", null)
                                .show()
                        }
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
         db = DatabaseHandler(context)
    }
}