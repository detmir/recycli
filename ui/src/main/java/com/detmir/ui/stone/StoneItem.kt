package com.detmir.ui.stone

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerState

class StoneItem {
    interface View {
        fun bindState(state: State)
    }

    @RecyclerState
    data class State(
        val id: String,
        val text: String
    ) : RecyclerItem {
        override fun provideId() = id
    }
}