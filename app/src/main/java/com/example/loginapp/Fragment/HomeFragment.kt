package com.example.loginapp.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.R
import com.example.loginapp.models.CallBack
import com.example.loginapp.models.FirebaseNoteDataManager
import com.example.loginapp.models.Note
import com.example.loginapp.models.NotesAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment() {
    private val TAG:String  = "HomeFragment"
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

//        noteDataManger.getAllNotes { mayBeNotes: ArrayList<Note>?, exception: Exception? ->
//            mayBeNotes?.let {
//                Log.d("checking all notes", "documents ${it}")
//                recyclerView.adapter = NotesAdapter(it)
//            }
//            exception?.let {
//                Toast.makeText(context,
//                        "Something went Wrong", Toast.LENGTH_SHORT).show()
//            }
//        }

        noteDataManger.getAllNotes (object: CallBack<ArrayList<Note>> {
            override fun onSuccess(data: ArrayList<Note>) {
                var notesAdapter = NotesAdapter(data)
                Log.e(TAG, "onNoteReceived: $data")
                notesAdapter = NotesAdapter(data)
                recyclerView.adapter = notesAdapter
                notesAdapter.notifyDataSetChanged()
            }

            override fun onFailure(exception: Exception) {
                Toast.makeText(context,
                        "Something went Wrong", Toast.LENGTH_SHORT).show()
            }
        })

        addNoteButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, CreateNoteFragment()).addToBackStack(null)
                commit()
            }
        }
    }
}
