package com.detmir.ui.test02

import androidx.annotation.ColorRes
import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

class Test02Item {


    @RecyclerItemState
    sealed class State(open val text: String) : RecyclerItem
    {
        data class One(
            val id: String = "ONE",
            override val text: String,
            @ColorRes val b: Int = 0
        ) : State(text) {
            override fun provideId() = id
        }

        sealed class Two(override val text: String) : State(text) {
            data class Two_1(
                val id: String = "TWO_1",
                override val text: String,
                @ColorRes val b: Int = 0
            ) : Two(text) {
                override fun provideId() = id
            }

            data class Two_2(
                val id: String = "TWO_2",
                override val text: String,
                @ColorRes val b: Int = 0
            ) : Two(text) {
                override fun provideId() = id
            }

            data class Two_3(
                val id: String = "TWO_2",
                override val text: String,
                @ColorRes val b: Int = 0
            ) : Two(text) {
                override fun provideId() = id
            }
        }

        data class Three(
            val id: String = "THREE",
            override val text: String,
            @ColorRes val b: Int = 0
        ) : State(text) {
            override fun provideId() = id
        }
    }


}