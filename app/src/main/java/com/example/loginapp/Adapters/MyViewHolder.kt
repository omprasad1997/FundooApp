package com.example.loginapp.Adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.Firebase.DataManager.Note
import com.example.loginapp.R
import com.example.loginapp.models.BaseViewHolder

class MyViewHolder(itemView: View, private val  notesList: ArrayList<Note>, onDeleteClick: (position:Int) -> Unit)  : BaseViewHolder(itemView) {
    private val notesTitle: TextView = itemView.findViewById(R.id.noteTitle)
    private val notesText : TextView = itemView.findViewById(R.id.notesText)

    init{
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener{
            onDeleteClick(adapterPosition)
        }
    }

    override fun onBind(position: Int) {
         val item = notesList[position]
        notesTitle.text = item.title
        notesText.text = item.notes
    }

    override fun clear() {
    }
}