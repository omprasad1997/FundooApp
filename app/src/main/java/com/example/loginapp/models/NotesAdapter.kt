package com.example.loginapp.models

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
        RecyclerView.Adapter<NotesAdapter.ViewHolder>(){
    private val firebaseNoteDataManager = FirebaseNoteDataManager()

     class ViewHolder(itemView: View, onDeleteClick: (position:Int) -> Unit)  : RecyclerView.ViewHolder(itemView){
        val notesTitle: TextView = itemView.findViewById(R.id.noteTitle)
        val notesText : TextView = itemView.findViewById(R.id.notesText)


         init{
             val firebaseNoteDataManager = FirebaseNoteDataManager()

             val deleteButton:ImageView = itemView.findViewById(R.id.deleteButton)
             deleteButton.setOnClickListener{
                 onDeleteClick(adapterPosition)
             }
         }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder:View = LayoutInflater.from(parent.context).inflate(R.layout.layout_notes_item, parent,
                false)
        return  ViewHolder(viewHolder, onDeleteClick = {position ->
            Log.e("Check delete","Item is deleted")
            firebaseNoteDataManager.deleteNote(notesList.get(position).id){
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.notesTitle.text = notesList[position].title
        holder.notesText.text = notesList[position].notes
    }
}
