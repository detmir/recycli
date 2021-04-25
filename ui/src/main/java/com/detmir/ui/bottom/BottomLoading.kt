package com.detmir.ui.bottom

import com.detmir.recycli.adapters.RecyclerBottomLoading
import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState


class BottomLoading : RecyclerBottomLoading {

    @RecyclerItemState
    sealed class State : RecyclerItem {
        override fun provideId(): String {
            return "bottom"
        }

        object Dummy : State()
        object Progress : State()
        data class Error(val reload: () -> Unit) : State()
        data class Button(val next: () -> Unit) : State()
    }

    override fun provideProgress(): RecyclerItem {
        return State.Progress
    }

    override fun provideDummy(): RecyclerItem {
        return State.Dummy
    }

    override fun provideError(reload: () -> Unit): RecyclerItem {
        return State.Error(reload)
    }

    override fun provideButton(next: () -> Unit): RecyclerItem {
        return State.Button(next)
    }

}