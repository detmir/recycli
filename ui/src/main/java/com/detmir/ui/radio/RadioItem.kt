package com.detmir.ui.radio

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

class RadioItem {
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