package com.detmir.kkppt3.views

import com.detmir.kkppt3.Case0400IntoView
import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

@RecyclerItemState
data class CloudItem(
    val id: String,
    val serverName: String,
    val intoView: Class<out Any>
) : RecyclerItem {
    override fun provideId() = id
    override fun withView() = intoView
}