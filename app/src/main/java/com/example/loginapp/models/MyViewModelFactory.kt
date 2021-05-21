package com.example.loginapp.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginapp.data.sqlite.NoteTableManager

class MyViewModelFactory(val noteTableManager: NoteTableManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NoteViewModel::class.java))
            return NoteViewModel(noteTableManager) as T
        throw IllegalAccessException("Failed to create viewmodel")
    }
}