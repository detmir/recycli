package com.detmir.kkppt3.views

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

@RecyclerItemState
data class UserItem(
    val id: String,
    val firstName: String,
    val online: Boolean,
    val onCardClick: (() -> Unit)? = null, //Optional
    val onMoveToOnline: (() -> Unit)? = null,
    val onMoveToOffline: (() -> Unit)? = null
) : RecyclerItem {
    override fun provideId() = id
}
