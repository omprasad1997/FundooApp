package com.example.loginapp.Dashboard.Activity.Fragments.Notes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.loginapp.Adapters.NotesAdapter
import com.example.loginapp.Firebase.DataManager.FirebaseNoteDataManager
import com.example.loginapp.Firebase.DataManager.Note
import com.example.loginapp.R
import com.example.loginapp.data.sqlite.DatabaseHandler
import com.example.loginapp.data.sqlite.NoteTable.NoteTableManagerImpl
import com.example.loginapp.models.MyViewModelFactory
import com.example.loginapp.models.NoteViewModel
import com.example.loginapp.models.ViewState
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment() {
    private val TAG: String = "HomeFragment"
    private lateinit var addNoteButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayout: ImageView

    private lateinit var gridLayout: ImageView
    private lateinit var firebaseNoteDataManager: FirebaseNoteDataManager
    private var notes: ArrayList<Note> = ArrayList<Note>()
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var noteViewModel: NoteViewModel
    private var isLinear = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initializationOfViews(view)
        setHasOptionsMenu(true)
        return view
    }

    private fun initializationOfViews(view: View?) {
        if (view != null) {
            addNoteButton = view.findViewById(R.id.addNoteButton)
            recyclerView = view.findViewById(R.id.recyclerView)
            linearLayout = view.findViewById(R.id.linearLayout)
            gridLayout = view.findViewById(R.id.gridLayout)

            recyclerView.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            firebaseNoteDataManager =
                FirebaseNoteDataManager()

            noteViewModel = ViewModelProvider(
                this, MyViewModelFactory(
                    NoteTableManagerImpl(
                        DatabaseHandler.getInstance(requireContext())
                    )
                )
            )
                .get(NoteViewModel::class.java)

            noteViewModel.notesMutableList.observe(viewLifecycleOwner,
                Observer { arrayListViewState ->
                    if (arrayListViewState is ViewState.Loading) {
                        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                    } else if (arrayListViewState is ViewState.Success) {
                        notes = arrayListViewState.data
                        Log.e(TAG, "onNoteReceived: $notes")
                        notesAdapter =
                            NotesAdapter(notes)
                        recyclerView.adapter = notesAdapter
                        notesAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(context, "Something went Wrong", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        addNoteButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(
                    R.id.fragment_container,
                    CreateNoteFragment()
                ).addToBackStack(null)
                commit()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        linearLayout.setOnClickListener {
                recyclerView.layoutManager =
                    StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                linearLayout.visibility = GONE
                gridLayout.visibility   = VISIBLE
            }

            gridLayout.setOnClickListener{
                    recyclerView.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    linearLayout.visibility = VISIBLE
                    gridLayout.visibility   = GONE
            }
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater.inflate(R.menu.search_menu, menu)

            val searchItem: MenuItem? = menu.findItem(R.id.searchAction)
            val searchView: SearchView = searchItem?.actionView as SearchView

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(text: String?): Boolean {
                    notesAdapter.filter.filter(text)
                    return false
                }
            })
    }
}
