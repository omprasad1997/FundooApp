package com.example.loginapp.Dashboard.Activity.Fragments.Notes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.loginapp.Adapters.NotesAdapter
import com.example.loginapp.Adapters.PaginationListener
import com.example.loginapp.Firebase.DataManager.FirebaseNoteDataManager
import com.example.loginapp.Firebase.DataManager.Note
import com.example.loginapp.HelperClasses.CallBack
import com.example.loginapp.R
import com.example.loginapp.data.sqlite.DatabaseHandler
import com.example.loginapp.data.sqlite.NoteTable.NoteTableManagerImpl
import com.example.loginapp.models.MyViewModelFactory
import com.example.loginapp.models.NoteViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private val TAG: String = "HomeFragment"
    private lateinit var addNoteButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayout: ImageView

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var gridLayout: ImageView
    private lateinit var firebaseNoteDataManager: FirebaseNoteDataManager
    private var notes: ArrayList<Note> = ArrayList<Note>()
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var isLastPage = false
    private val LIMIT = 10
    private var isLoading = false
    var itemCount = 0
    private var TOTAL_NOTES_COUNT = 0
    private var CURRENT_NOTES_COUNT = 0

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
            swipeRefresh = view.findViewById(R.id.swipeRefresh)

            swipeRefresh.setOnRefreshListener(this)

            recyclerView.setHasFixedSize(true)


            val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

            // set the above layout manager rather than creating new
            recyclerView.layoutManager = layoutManager
            // initialize notes adapter
            notesAdapter = NotesAdapter(ArrayList())
            recyclerView.adapter = notesAdapter
            firebaseNoteDataManager =
                FirebaseNoteDataManager()

            noteViewModel = ViewModelProvider(
                this, MyViewModelFactory(
                    NoteTableManagerImpl(
                        DatabaseHandler.getInstance(requireContext())
                    )
                )
            ).get(NoteViewModel::class.java)

            // fetch the notes initially
            fetchNotes(0)

            recyclerView.addOnScrollListener(object : PaginationListener(layoutManager) {
                override fun loadMoreItems() {
                    isLoading = true

                    // Fix to this issue - https://stackoverflow.com/questions/42944005/recyclerview-cannot-call-this-method-in-a-scroll-callback
                    recyclerView.post { notesAdapter.addLoading() }
                    fetchNotes(notesAdapter.getItem(CURRENT_NOTES_COUNT - 1).creationTime)

//                    fetchNotes(notesAdapter.getItem(notesAdapter.itemCount - 2) as Long)
                }

                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }

            })
//            noteViewModel.notesMutableList.observe(viewLifecycleOwner,
//                Observer { arrayListViewState ->
//                    if (arrayListViewState is ViewState.Loading) {
//                        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
//                    } else if (arrayListViewState is ViewState.Success) {
//                        notes = arrayListViewState.data
//                        Log.e(TAG, "onNoteReceived: $notes")
//                        notesAdapter =
//                            NotesAdapter(notes)
//                        recyclerView.adapter = notesAdapter
//                        notesAdapter.notifyDataSetChanged()
//                    } else {
//                        Toast.makeText(context, "Something went Wrong", Toast.LENGTH_SHORT).show()
//                    }
//                })
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

    private fun fetchNotes(timestamp: Long) {
        fetchAllNotesSize(object : CallBack<Int?> {
            override fun onSuccess(data: Int?) {
                if (data != null) {
                    TOTAL_NOTES_COUNT = data
                }
                Log.e(TAG, "onSuccess: total notes count $data")
                val noteslist: ArrayList<Note> = ArrayList<Note>()
                firebaseUser = FirebaseAuth.getInstance().currentUser!!
                firebaseFirestore = FirebaseFirestore.getInstance()
                firebaseFirestore.collection("Users").document(firebaseUser.uid)
                    .collection("noteList")
                    .orderBy("creation time")
                    .startAfter(timestamp)
                    .limit(LIMIT.toLong())
                    .get()
                    .addOnSuccessListener { queryDocumentSnapshots ->
                        var i: Int = 0
                        val size = queryDocumentSnapshots.size()
                        while (i < size) {
                            val documentSnapshot =
                                queryDocumentSnapshots.documents[i]
                            val title = documentSnapshot.getString("title")
                            val content = documentSnapshot.getString("note")
                            val docID = documentSnapshot.id
                            val timestamp = documentSnapshot.getLong("creation time")!!
                            val note = Note(title, content, docID)
                            note.creationTime = timestamp
                            noteslist.add(note)
                            i++
                        }

                        if (notesAdapter.itemCount > 0)
                            notesAdapter.removeLoading()
                        isLoading = false
                        CURRENT_NOTES_COUNT += size
                        Log.e(TAG, "snapshots size $size")
                        notesAdapter.addItems(noteslist)

                        if (CURRENT_NOTES_COUNT < TOTAL_NOTES_COUNT) {
                            Log.e(
                                TAG,
                                "onSuccess: Current & Total $CURRENT_NOTES_COUNT : $TOTAL_NOTES_COUNT"
                            )
                        } else {
                            Log.e(
                                TAG,
                                "onSuccess: is last page true $CURRENT_NOTES_COUNT : $TOTAL_NOTES_COUNT"
                            )
                            isLastPage = true
                        }
                    }
            }

            override fun onFailure(exception: Exception) {}
        })
    }

    private fun fetchAllNotesSize(countCallBack: CallBack<Int?>) {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection("Users").document(firebaseUser.uid)
            .collection("noteList")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                countCallBack.onSuccess(
                    queryDocumentSnapshots.size()
                )
            }.addOnFailureListener { e -> countCallBack.onFailure(e) }
    }

    override fun onResume() {
        super.onResume()
        linearLayout.setOnClickListener {
            recyclerView.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            linearLayout.visibility = GONE
            gridLayout.visibility = VISIBLE
        }

        gridLayout.setOnClickListener {
            recyclerView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            linearLayout.visibility = VISIBLE
            gridLayout.visibility = GONE
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

    override fun onRefresh() {
        swipeRefresh.isRefreshing = false
    }
}
