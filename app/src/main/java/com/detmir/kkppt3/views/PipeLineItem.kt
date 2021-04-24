package com.detmir.kkppt3.views

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

@RecyclerItemState
sealed class PipeLineItem : RecyclerItem {
    data class Input(
        val id: String,
        val from: String
    ) : PipeLineItem() {
        override fun provideId() = id
    }

    data class Output(
        val id: String,
        val to: String
    ) : PipeLineItem() {
        override fun provideId() = id
    }
}
