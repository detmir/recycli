package com.detmir.ui.label

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

class LabelItem {


    @RecyclerItemState
    sealed class State(open val text: String) : RecyclerItem
    {
        data class Big(
            val id: String,
            override val text: String
        ) : State(text) {
            override fun provideId() = id
        }

        data class Small(
            val id: String,
            override val text: String
        ) : State(text) {
            override fun provideId() = id
        }
    }


}