package com.example.loginapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.loginapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment() {
    private lateinit var addNoteButton: FloatingActionButton

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
        }
        addNoteButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, CreateNoteFragment()).addToBackStack(null)
                commit()
            }
        }
    }
}