package com.detmir.kkppt3.views

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerState

@RecyclerState
data class HeaderItem(
    val id: String,
    val title: String
) : RecyclerItem {
    override fun provideId() = id
}
