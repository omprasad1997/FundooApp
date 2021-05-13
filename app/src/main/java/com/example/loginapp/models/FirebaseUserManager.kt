package com.example.loginapp.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseUserManager {

    private val TAG = "FirebaseUserManager"
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    fun getUserDetails(listener: CallBack<FirebaseUserModel?>) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        firebaseFirestore.collection("Users")
                .document(firebaseUser.uid).get()
                .addOnSuccessListener { documentSnapshots ->
                    val firebaseUserModel = FirebaseUserModel(
                            documentSnapshots.getString("Name")!!
                            , documentSnapshots.getString("email")!!)
                    listener.onSuccess(firebaseUserModel)
                }
                .addOnFailureListener { e -> listener.onFailure(e) }
    }
}