package com.example.loginapp.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.Class.FirebaseNoteDataManager
import com.example.loginapp.Class.NotesAdapter
import com.example.loginapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment() {
    private lateinit var addNoteButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteDataManger: FirebaseNoteDataManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initializationOfViews(view)
        return view
    }

    private fun initializationOfViews(view: View?) {
        if (view != null) {
            addNoteButton = view.findViewById(R.id.addNoteButton)
            recyclerView = view.findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(context)
            noteDataManger = FirebaseNoteDataManager()
        }

        noteDataManger.getAllNotes {
            Log.d("Omprasad", "documents ${it}")
            recyclerView.adapter = NotesAdapter(it)
        }
        addNoteButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, CreateNoteFragment()).addToBackStack(null)
                commit()
            }
        }
    }
}
