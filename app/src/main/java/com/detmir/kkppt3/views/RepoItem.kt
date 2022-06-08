package com.detmir.kkppt3.views

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

@RecyclerItemState
data class RepoItem(
    val id: String,
    val repoName: String,
    val i: Int,
    val onPlus: () -> Unit
) : RecyclerItem {
    override fun provideId() = id
}
