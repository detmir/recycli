package com.detmir.recycli.adapters

import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil

@Keep
class RecyclerDiffItemCallback() : DiffUtil.ItemCallback<RecyclerItem>() {
    override fun areItemsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
        return oldItem.provideId() == newItem.provideId()
    }

    override fun areContentsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }
}
