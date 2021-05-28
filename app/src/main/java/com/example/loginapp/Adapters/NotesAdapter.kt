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
import com.example.loginapp.models.BaseViewHolder

class NotesAdapter(private var notesList: ArrayList<Note>) :
    RecyclerView.Adapter<BaseViewHolder>(), Filterable {

    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1
    private var isLoaderVisible = false

    private var cloneOfNoteList = ArrayList<Note>(notesList)
    private val firebaseNoteDataManager =
        FirebaseNoteDataManager()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        if (viewType == VIEW_TYPE_NORMAL) {
            val viewHolder: View = LayoutInflater.from(parent.context).inflate(
                R.layout.layout_notes_item, parent,
                false
            )
            return MyViewHolder(
                viewHolder, notesList,
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
        } else {
            val viewHolder: View = LayoutInflater.from(parent.context).inflate(
                R.layout.layout_notes_item, parent,
                false
            )
            return ProgessHolder(viewHolder)
        }
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
//        holder.notesTitle.text = notesList[position].title
//        holder.notesText.text = notesList[position].notes
    }

    override fun getItemViewType(position: Int): Int {
        if (isLoaderVisible) {
            return if (position == notesList.size - 1)
                VIEW_TYPE_LOADING
            else
                VIEW_TYPE_NORMAL
        } else {
            return VIEW_TYPE_NORMAL
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun addItems(noteItems: List<Note>) {
        notesList.addAll(noteItems)
        notifyDataSetChanged()
    }

    fun addLoading() {
        isLoaderVisible = true
//        notesList.add(Note())
        notifyItemInserted(notesList.size - 1)
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position = notesList.size - 1
        val item = getItem(position)
        notesList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        notesList.clear()
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Any {
        return notesList[position]
    }


    override fun getFilter(): Filter {
        return exampleFilter
    }

    private var exampleFilter: Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: MutableList<Note> = ArrayList()

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

            val results = FilterResults()
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
