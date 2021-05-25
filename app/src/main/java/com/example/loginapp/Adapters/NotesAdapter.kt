package com.example.loginapp.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.R
import com.example.loginapp.Firebase.DataManager.FirebaseNoteDataManager
import com.example.loginapp.Firebase.DataManager.Note

class NotesAdapter(private var notesList: ArrayList<Note>) :
    RecyclerView.Adapter<MyViewHolder>(), Filterable {

    private var cloneOfNoteList = ArrayList<Note>(notesList)
    private val firebaseNoteDataManager =
        FirebaseNoteDataManager()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewHolder: View = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_notes_item, parent,
            false
        )
        return MyViewHolder(
            viewHolder,
            onDeleteClick = { position ->
                Log.e("Check delete", "Item is deleted")
                firebaseNoteDataManager.deleteNote(notesList[position].id) {
                    if (it) {
                        Toast.makeText(
                            parent.context, "Note is deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        notesList.removeAt(position)
                        notifyItemRemoved(position)
                    } else {
                        Toast.makeText(
                            parent.context, "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
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

    override fun getFilter(): Filter {
        return exampleFilter
    }

    private var exampleFilter: Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var filteredList: MutableList<Note> = ArrayList()

            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(cloneOfNoteList)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()
                for (item in cloneOfNoteList) {
                    if (item.title?.toLowerCase()?.contains(filterPattern)!!) {
                        filteredList.add(item)
                    }
                }
            }

            var results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notesList.clear()
            notesList.addAll(results?.values as Collection<Note>)
            notifyDataSetChanged()
        }
    }
}
