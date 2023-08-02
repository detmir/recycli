package com.detmir.recycli.adapters

import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil

@Keep
class RecyclerDiffCallback : DiffUtil.Callback() {

    lateinit var old: MutableList<RecyclerItem>
    lateinit var aNew: MutableList<RecyclerItem>

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].provideId() == aNew[newItemPosition].provideId()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].areContentsTheSame(aNew[newItemPosition])
    }

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return aNew.size
    }
}
