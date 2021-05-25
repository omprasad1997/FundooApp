package com.example.loginapp.Adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.R

class MyViewHolder(itemView: View, onDeleteClick: (position:Int) -> Unit)  : RecyclerView.ViewHolder(itemView){
    val notesTitle: TextView = itemView.findViewById(R.id.noteTitle)
    val notesText : TextView = itemView.findViewById(R.id.notesText)

    init{
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener{
            onDeleteClick(adapterPosition)
        }
    }
}