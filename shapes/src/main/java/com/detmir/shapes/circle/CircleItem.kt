package com.detmir.shapes.circle

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

class CircleItem {
    interface View {
        fun bindState(state: State)
    }

    @RecyclerItemState
    data class State(
        val id: String,
        val text: String
    ) : RecyclerItem {
        override fun provideId() = id
    }
}