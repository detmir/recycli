package com.detmir.kkppt3.views

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

@RecyclerItemState
data class UserItem(
    val id: String,
    val firstName: String,
    val online: Boolean,
    val onCardClick: ((String) -> Unit)? = null, //Optional
    val onMoveToOnline: ((String) -> Unit)? = null,
    val onMoveToOffline: ((String) -> Unit)? = null
) : RecyclerItem {
    override fun provideId() = id
}
