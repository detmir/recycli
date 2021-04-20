package com.detmir.kkppt3.views

import androidx.annotation.ColorRes
import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerState

@RecyclerState
data class BigTask(
    val id: String,
    val title: String,
    val description: String,
    val onCardClick: (() -> Unit)? = null
) : RecyclerItem {
    override fun provideId() = id
}
