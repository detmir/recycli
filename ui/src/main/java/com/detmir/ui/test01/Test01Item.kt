package com.detmir.ui.test01

import androidx.annotation.ColorRes
import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

class Test01Item {
    interface View {
        fun bindState(state: State)
    }

    @RecyclerItemState
    data class State(
        val intoView: Class<out android.view.View>? = null,// = Test01RedItemView::class.java,
        val id: String,
        val text: String,
        val heightPx: Int? = null,
        @ColorRes val delimiterBackgroundColor: Int? = null,
        @ColorRes val backgroundColor: Int? = null
    ) : RecyclerItem {
        override fun provideId() = id
        override fun withView() = intoView
    }
}