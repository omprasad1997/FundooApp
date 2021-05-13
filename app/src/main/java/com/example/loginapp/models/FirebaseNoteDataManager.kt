package com.example.loginapp.models

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseNoteDataManager {
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    fun saveDataToFireStoreWithSubCollection(title: String, note: String, view: View?) {
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val notesInformation = mutableMapOf<String, Any>()
        val usersInformation = mutableMapOf<String, Any>()
        val userUID = mAuth.currentUser?.uid
        notesInformation["title"] = title
        notesInformation["note"] = note
        usersInformation["Name"] = mAuth.currentUser?.displayName.toString()
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

//    fun getAllNotes(completion: (ArrayList<Note>?, Exception?) -> Unit) {
//        db = FirebaseFirestore.getInstance()
//        mAuth = FirebaseAuth.getInstance()
//
//        val userUid = mAuth.currentUser?.uid
//        db.collection("Users").document(userUid.toString())
//                .collection("noteList")
//                .get()
//                .addOnCompleteListener { task ->
//                    if (!task.isSuccessful) {
//                        completion(null, task.exception)
//                        return@addOnCompleteListener
//                    }
//
//                    val list = task.result?.documents?.map { doc ->
//                        return@map Note(id = doc.id, title = doc.getString("title"),
//                                notes = doc.getString("note"))
//                    }
//                    completion(ArrayList(list ?: emptyList()), null)
//                }
//    }

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

                        Log.e("NoteManager",
                                "onSuccess: " + queryDocumentSnapshots
                                        .documents[index])
                        val documentId = queryDocumentSnapshots.documents[index].id
                        val title = queryDocumentSnapshots.documents[index].getString("title")
                        val notes = queryDocumentSnapshots.documents[index].getString("note")
                        val note = Note(documentId, title, notes)
                        notesList.add(note)
                    }
                    listener.onSuccess(notesList)
                }
                .addOnFailureListener { e -> listener.onFailure(e) }
    }
}