package com.example.loginapp.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.R
import com.example.loginapp.models.*
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment() {
    private val TAG:String  = "HomeFragment"
    private lateinit var addNoteButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var firebaseNoteDataManager: FirebaseNoteDataManager
    private var notes: ArrayList<Note> = ArrayList<Note>()
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var noteViewModel: NoteViewModel

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
            firebaseNoteDataManager = FirebaseNoteDataManager()

            noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

            noteViewModel.notesMutableList.observe(viewLifecycleOwner,
                Observer<ViewState<ArrayList<Note>>> { arrayListViewState ->
                    if(arrayListViewState is ViewState.Loading) {
                        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show();
                    } else if (arrayListViewState is ViewState.Success) {
                        notes = arrayListViewState.data
                        Log.e(TAG, "onNoteReceived: $notes")
                        notesAdapter = NotesAdapter(notes)
                        recyclerView.adapter = notesAdapter
                        notesAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(context, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
                })
        }



//        noteDataManger.getAllNotes (object: CallBack<ArrayList<Note>> {
//            override fun onSuccess(data: ArrayList<Note>) {
//                var notesAdapter = NotesAdapter(data)
//                Log.e(TAG, "onNoteReceived: $data")
//                notesAdapter = NotesAdapter(data)
//                recyclerView.adapter = notesAdapter
//                notesAdapter.notifyDataSetChanged()
//            }
//
//            override fun onFailure(exception: Exception) {
//                Toast.makeText(context,
//                        "Something went Wrong", Toast.LENGTH_SHORT).show()
//            }
//        })

        addNoteButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, CreateNoteFragment()).addToBackStack(null)
                commit()
            }
        }
    }
}
