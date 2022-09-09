package com.zed.wannawatch.ui.screens.search


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.zed.wannawatch.R
import com.zed.wannawatch.services.api.models.SearchResult
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.ui.util.OnClickListener

// https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
class SearchListAdapter(private val onClickListener: OnClickListener<SearchResult>): ListAdapter<SearchResult, SearchListAdapter.ViewHolder>(SearchDiffCallback) {

    /*
    by implementing the List adapter i don't need to keep track of the list of data
    or notify when the dataset changes.

    to add new data simple call the submitList(data) of ListAdapter
    to get an item use getItem(position)

    https://www.section.io/engineering-education/handling-recyclerview-clicks-the-right-way/
     */

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {

        val posterImage: ImageView
        val container: FrameLayout

        init {
            posterImage = view.findViewById(R.id.posterImage)
            container = view.findViewById(R.id.movieFrame)
        }

        fun bind(item: SearchResult) {
            Log.d("SearchListAdapter", "ViewHolder.bind")
            // load the poster image using coil
            posterImage.load(item.poster) {
                crossfade(true)
            }
        }
    }



    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.movie_grid_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.container.setOnClickListener {
            onClickListener.onClick(item)
        }
    }
}


object SearchDiffCallback: DiffUtil.ItemCallback<SearchResult>() {
    override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem == newItem
    }

}