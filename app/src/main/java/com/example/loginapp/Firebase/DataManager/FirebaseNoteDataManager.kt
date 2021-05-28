package com.example.loginapp.Firebase.DataManager

import android.util.Log
import com.example.loginapp.HelperClasses.CallBack
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseNoteDataManager {
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    fun saveDataToFireStoreWithSubCollection(title: String, note: String,
                                             completion: (id: String?, exception:Exception?) -> Unit) {
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val notesInformation = mutableMapOf<String, Any>()
        val usersInformation = mutableMapOf<String, Any>()
        val userUID = mAuth.currentUser?.uid
        notesInformation["title"] = title
        notesInformation["note"] = note
        notesInformation["creation time"] = System.currentTimeMillis()
        usersInformation["Name"] = mAuth.currentUser?.displayName.toString()
        usersInformation["email"] = mAuth.currentUser?.email.toString()
        db.collection("Users").document(userUID.toString()).set(usersInformation)
        val noteDocumentReference = db.collection("Users").document(userUID.toString())
            .collection("noteList").document()

        noteDocumentReference.set(notesInformation)
            .addOnSuccessListener {
                completion(noteDocumentReference.id,null)
            }
            .addOnFailureListener {
                completion(null, it)
            }
    }

    fun deleteNote(id: String?, completion: (isSuccessful: Boolean) -> Unit) {
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val userUid = mAuth.currentUser?.uid


        id?.let {
            db.collection("Users").document(userUid.toString())
                .collection("noteList")
                .document(it)
                .delete()
                .addOnCompleteListener { task ->
                    completion(task.isSuccessful)
                }
        }
    }

    fun getAllNotes(listener: CallBack<ArrayList<Note>>) {
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val userUid = mAuth.currentUser?.uid
        val notesList: ArrayList<Note> = ArrayList<Note>()

        db.collection("Users").document(userUid.toString())
            .collection("noteList")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (index in 0 until queryDocumentSnapshots.size()) {

                    Log.e(
                        "NoteManager",
                        "onSuccess: " + queryDocumentSnapshots
                            .documents[index]
                    )
                    val documentId = queryDocumentSnapshots.documents[index].id
                    val title = queryDocumentSnapshots.documents[index].getString("title")
                    val notes = queryDocumentSnapshots.documents[index].getString("note")
                    val note = Note(
                        documentId,
                        title,
                        notes,
                        userUid
                    )
                    notesList.add(note)
                }
                listener.onSuccess(notesList)
            }
            .addOnFailureListener { e -> listener.onFailure(e) }
    }
}