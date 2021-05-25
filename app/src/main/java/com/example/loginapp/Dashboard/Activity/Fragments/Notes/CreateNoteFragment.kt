package com.example.loginapp.Dashboard.Activity.Fragments.Notes


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
import com.example.loginapp.data.sqlite.NoteTable.NoteTableManager
import com.example.loginapp.data.sqlite.NoteTable.NoteTableManagerImpl
import com.example.loginapp.Firebase.DataManager.FirebaseNoteDataManager
import com.example.loginapp.Firebase.DataManager.Note
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class CreateNoteFragment : Fragment() {
    private lateinit var writtenNote: EditText
    private lateinit var noteTitle: EditText
    private lateinit var saveNoteButton: ImageView
    private lateinit var firebaseNoteDataManager: FirebaseNoteDataManager
    private lateinit var mAuth: FirebaseAuth
    private lateinit var noteTableManager: NoteTableManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        val view = inflater.inflate(R.layout.fragment_create_note, container, false)
        initializationOfViews(view)
        return view
    }

    private fun initializationOfViews(view: View?) {
        if (view != null) {
            writtenNote = view.findViewById(R.id.writtenNote)
            noteTitle = view.findViewById(R.id.noteTitle)
            saveNoteButton = view.findViewById(R.id.saveNoteButton)
            firebaseNoteDataManager =
                FirebaseNoteDataManager()
            mAuth = FirebaseAuth.getInstance()
            noteTableManager =
                NoteTableManagerImpl(
                    DatabaseHandler.getInstance(requireContext())
                )

            saveNoteButton.setOnClickListener {

                val title = noteTitle.text.toString()
                val note = writtenNote.text.toString()
                val userUid = mAuth.currentUser?.uid

                if (note.isEmpty() && title.isEmpty()) {
                    Snackbar.make(it, "Empty note discarded", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
                    activity?.onBackPressed()
                } else {
                    firebaseNoteDataManager.saveDataToFireStoreWithSubCollection(
                        title, note
                    ) { noteId: String?, exception: Exception? ->
                        noteId?.let {
                            noteTableManager.addNote(
                                Note(
                                    noteId,
                                    title,
                                    note,
                                    userUid
                                )
                            )
                            Snackbar.make(view, "Note saved successfully", Snackbar.LENGTH_LONG)
                                .setAction("Action", null)
                                .show()
                            activity?.onBackPressed()
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

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }
}