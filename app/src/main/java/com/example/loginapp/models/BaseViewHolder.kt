package com.example.loginapp.models

import android.view.View
import androidx.recyclerview.widget.RecyclerView


abstract class BaseViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    private var currentPosition = 0

    protected abstract fun clear()

    fun onBind(position: Int) {
        currentPosition = position
        clear()
    }

}