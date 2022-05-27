package com.detmir.kkppt3.views

import androidx.annotation.ColorRes
import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

@RecyclerItemState
data class HeaderItem(
    val id: String,
    val title: String,
    @ColorRes val background: Int = android.R.color.transparent
) : RecyclerItem {
    override fun provideId() = id
}
