package com.detmir.ui.avatar

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

class AvatarItem {


    @RecyclerItemState
    data class State(
        val view: VIEWS,
        val id: String,
        val text: String
    ) : RecyclerItem {
        override fun provideId() = id
        override fun withView() = view.clazz
    }

    enum class VIEWS (val clazz: Class<out android.view.View>) {
        SQAURED(AvatarItemSquaredCornersView::class.java),
        ROUNDED(AvatarItemRoundedCornersView::class.java)
    }

    companion object {

    }
}