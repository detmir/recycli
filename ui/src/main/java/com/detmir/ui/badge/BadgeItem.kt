package com.detmir.ui.badge

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

@RecyclerItemState
data class BadgeItem(
    val id: String,
    val message: String
) : RecyclerItem {
    override fun provideId(): String = id
}