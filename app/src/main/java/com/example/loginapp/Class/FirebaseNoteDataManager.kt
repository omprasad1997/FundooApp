package com.example.loginapp.Class

import com.example.loginapp.models.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseNoteDataManager {
    fun getAllNotes(completion: (List<Note>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val mAuth = FirebaseAuth.getInstance()

        val userUid = mAuth.currentUser?.uid
        db.collection("Users").document(userUid.toString())
                .collection("noteList")
                .get()
                .addOnCompleteListener {
                    val list = it.result?.documents?.map { doc ->
                        return@map Note(doc.getString("title"), doc.getString("note"))
                    }
                    completion(list ?: emptyList())
                }
    }
}