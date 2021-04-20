package com.detmir.shapes.line

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerState

class LineItem {
    interface View {
        fun bindState(state: State)
    }

    @RecyclerState
    sealed class State: RecyclerItem {
        data class Bezier(
            val id: String,
            val text: String
        ) : State() {
            override fun provideId() = id
        }
        data class Arc(
            val id: String,
            val text: String
        ) : State() {
            override fun provideId() = id
        }
    }
}