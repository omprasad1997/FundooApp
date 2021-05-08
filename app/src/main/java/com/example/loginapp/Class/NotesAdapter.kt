package com.example.loginapp.Class

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.R
import com.example.loginapp.models.Note

class NotesAdapter(private var notesList: List<Note>) :
        RecyclerView.Adapter<NotesAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val notesTitle: TextView = itemView.findViewById(R.id.noteTitle)
        val notesText : TextView = itemView.findViewById(R.id.notesText)

        init {
            itemView.setOnClickListener{
                val position:Int = adapterPosition
                Toast.makeText(itemView.context, "You clicked on item = ${position + 1}",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v:View = LayoutInflater.from(parent.context).inflate(R.layout.layout_notes_item, parent,
                false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.notesTitle.text = notesList[position].title
        holder.notesText.text = notesList[position].notes
    }
}