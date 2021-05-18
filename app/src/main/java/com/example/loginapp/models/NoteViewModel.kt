package com.example.loginapp.models

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NoteViewModel : ViewModel() {

    val notesMutableList: MutableLiveData<ViewState<ArrayList<Note>>> =
        MutableLiveData()
    private val TAG: String = "NotesViewModel"
    private var firebaseNoteDataManager = FirebaseNoteDataManager()

    init {
        loadNotes()
    }

    private fun loadNotes(){
        notesMutableList.value = ViewState.Loading()
        firebaseNoteDataManager.getAllNotes (object: CallBack<ArrayList<Note>> {
            override fun onSuccess(data: ArrayList<Note>) {
                Log.e(TAG, "onNoteReceived: $data")
                notesMutableList.value = ViewState.Success(data)
            }

            override fun onFailure(exception: Exception) {
                Log.e(TAG, "Something went wrong: $exception")
                notesMutableList.value = ViewState.Failure(exception)
            }
        })
    }
}