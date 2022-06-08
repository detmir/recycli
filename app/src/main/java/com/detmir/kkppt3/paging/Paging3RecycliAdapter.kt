package com.detmir.kkppt3.paging

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.detmir.recycli.adapters.RecyclerBaseAdapter
import com.detmir.recycli.adapters.RecyclerDiffItemCallback
import com.detmir.recycli.adapters.RecyclerItem

class Paging3RecycliAdapter :
    PagingDataAdapter<RecyclerItem, RecyclerView.ViewHolder>(RecyclerDiffItemCallback()) {

    private val recyclerBaseAdapter = RecyclerBaseAdapter(
        binders = null,
        getRecyclerItem = { pos ->
            getItem(pos) as RecyclerItem
        }
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        recyclerBaseAdapter.onBindViewHolder(holder, position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        recyclerBaseAdapter.onCreateViewHolder(parent, viewType)

    override fun getItemViewType(position: Int): Int = recyclerBaseAdapter.getItemViewType(position)
}
