package com.example.loginapp.models

import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.R

class NotesAdapter(private var notesList: ArrayList<Note>) :
        RecyclerView.Adapter<MyViewHolder>(){
    private val firebaseNoteDataManager = FirebaseNoteDataManager()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewHolder:View = LayoutInflater.from(parent.context).inflate(R.layout.layout_notes_item, parent,
                false)
        return  MyViewHolder(viewHolder, onDeleteClick = {position ->
            Log.e("Check delete","Item is deleted")
            firebaseNoteDataManager.deleteNote(notesList[position].id){
                if(it){
                    Toast.makeText(parent.context, "Note is deleted",
                            Toast.LENGTH_SHORT).show()
                    notesList.removeAt(position)
                    notifyItemRemoved(position)
                }else{
                    Toast.makeText(parent.context, "Something went wrong",
                            Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.notesTitle.text = notesList[position].title
        holder.notesText.text = notesList[position].notes
    }
}
