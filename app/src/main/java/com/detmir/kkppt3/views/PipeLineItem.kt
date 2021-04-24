package com.detmir.kkppt3.views

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

@RecyclerItemState
sealed class PipeLineItem : RecyclerItem {
    abstract val id: String

    data class Input(
        override val id: String,
        val from: String
    ) : PipeLineItem()

    data class Output(
        override val id: String,
        val to: String
    ) : PipeLineItem()

    override fun provideId() = id
}
