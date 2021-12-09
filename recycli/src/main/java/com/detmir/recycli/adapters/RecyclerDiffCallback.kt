package com.detmir.recycli.adapters

import androidx.recyclerview.widget.DiffUtil

class RecyclerDiffCallback(
    private val old: List<RecyclerItem>,
    private val new: List<RecyclerItem>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean =
        old[oldItemPosition].provideId() == new[newItemPosition].provideId()

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean =
        old[oldItemPosition].areContentsTheSame(new[newItemPosition])

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

}
