package com.detmir.kkppt3.paging

import android.util.Log
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.detmir.recycli.adapters.RecyclerBaseAdapter
import com.detmir.recycli.adapters.RecyclerDiffItemCallback
import com.detmir.recycli.adapters.RecyclerItem

class RecycliPagingAdapter(placeHolderProvider: ((Int) -> RecyclerItem)?) :
    PagingDataAdapter<RecyclerItem, RecyclerView.ViewHolder>(RecyclerDiffItemCallback()) {

    private val recyclerBaseAdapter = RecyclerBaseAdapter(
        getRecyclerItem = { pos ->
            var item = getItem(pos)
            if (item == null) {
                item = placeHolderProvider?.invoke(pos)
            }
            if (item == null) throw Exception("Not found item for pos=$pos")
            item
        }
    )

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerBaseAdapter.warmUpBinders(recyclerView.context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        recyclerBaseAdapter.onBindViewHolder(holder, position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("pager3", "viewType = $viewType")
        return recyclerBaseAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return recyclerBaseAdapter.getItemViewType(position)
    }

}
