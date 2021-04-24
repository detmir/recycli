package com.detmir.kkppt3.views

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.adapters.ScrollKeeper
import com.detmir.recycli.annotations.RecyclerItemState

@RecyclerItemState
data class SimpleContainerItem(
    val id: String,
    val recyclerState: List<RecyclerItem>
): RecyclerItem {
    override fun provideId(): String {
        return id
    }
}