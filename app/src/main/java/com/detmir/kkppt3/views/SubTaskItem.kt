package com.detmir.kkppt3.views

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

@RecyclerItemState
data class SubTaskItem(
    val id: String,
    val title: String,
    val description: String,
    val onCardClick: (() -> Unit)? = null
) : RecyclerItem {
    override fun provideId() = id
}
