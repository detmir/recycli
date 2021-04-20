package com.detmir.ui.button

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerState


class ButtonItem {


    @RecyclerState
    sealed class State(open val text: String) : RecyclerItem
    {
        data class Error(
            val id: String,
            override val text: String
        ) : State(text) {
            override fun provideId() = id
        }

        sealed class Colored(override val text: String) : State(text) {
            data class Green(
                val id: String,
                override val text: String
            ) : Colored(text) {
                override fun provideId() = id
            }

            data class Orange(
                val id: String,
                override val text: String
            ) : Colored(text) {
                override fun provideId() = id
            }
        }

        data class Loading(
            val id: String,
            override val text: String
        ) : State(text) {
            override fun provideId() = id
        }
    }


}