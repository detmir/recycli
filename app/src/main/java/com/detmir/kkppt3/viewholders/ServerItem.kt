package com.detmir.kkppt3.viewholders

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

@RecyclerItemState
data class ServerItem(
    val id: String,
    val serverAddress: String
) : RecyclerItem {
    override fun provideId() = id
}
