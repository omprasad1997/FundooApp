package com.example.loginapp.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loginapp.data.sqlite.NoteTableManager
import com.example.loginapp.util.Note

class NoteViewModel(private val noteTableManager: NoteTableManager) : ViewModel() {

    val notesMutableList: MutableLiveData<ViewState<ArrayList<Note>>> =
        MutableLiveData()
    private val TAG: String = "NotesViewModel"
    private var firebaseNoteDataManager =
        FirebaseNoteDataManager()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        notesMutableList.value = ViewState.Loading(ArrayList(noteTableManager.getAllNotes()))
        firebaseNoteDataManager.getAllNotes(object : CallBack<ArrayList<Note>> {
            override fun onSuccess(data: ArrayList<Note>) {
                Log.e(TAG, "onNoteReceived: $data")
                noteTableManager.deleteAllNotes()
                noteTableManager.addAllNotes(data)
                notesMutableList.value = ViewState.Success(ArrayList(noteTableManager.getAllNotes()))
            }

            override fun onFailure(exception: Exception) {
                Log.e(TAG, "Something went wrong: $exception")
                notesMutableList.value =
                    ViewState.Failure(exception, ArrayList(noteTableManager.getAllNotes()))
            }
        })
    }
}