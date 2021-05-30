package com.example.loginapp.Adapters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


abstract class PaginationListener(private val layoutManager: StaggeredGridLayoutManager) : RecyclerView.OnScrollListener() {

    /**
     * Set scrolling threshold here (for now i'm assuming 10 item in one page)
     */
    private val ITEMS_LIMIT_PER_PAGE = 10

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPositions(null)[0]

//        Log.e(TAG, "onScrolled: loading ${isLoading()}")
//        Log.e(TAG, "onScrolled: last page ${isLastPage()}")
        if (!isLoading() && !isLastPage()) {
            val hasReachedEndOfList = visibleItemCount + firstVisibleItemPosition >= totalItemCount
            if (hasReachedEndOfList
                && firstVisibleItemPosition >= 0 && totalItemCount >= ITEMS_LIMIT_PER_PAGE
            ) {
                loadMoreItems()
                Log.e(TAG, "onScrolled: scroll detected")
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean

    companion object {
        private const val TAG = "PaginationListener"
    }
}