package com.detmir.ui.bottom

import com.detmir.recycli.adapters.RecyclerBottomLoading
import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState


class BottomLoading : RecyclerBottomLoading {

    @RecyclerItemState
    sealed class State : RecyclerItem {
        object Dummy : State() {
            override fun provideId() = BOTTOM_DUMMY
        }
        object Progress : State() {
            override fun provideId() = BOTTOM_PROGRESS
        }
        data class Error(val reload: () -> Unit) : State() {
            override fun provideId() = BOTTOM_ERROR
        }
        data class Button(val next: () -> Unit) : State() {
            override fun provideId() = BOTTOM_BUTTON
        }
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


    companion object {
        const val BOTTOM_DUMMY  = "BOTTOM_DUMMY"
        const val BOTTOM_PROGRESS  = "BOTTOM_PROGRESS"
        const val BOTTOM_ERROR  = "BOTTOM_ERROR"
        const val BOTTOM_BUTTON  = "BOTTOM_BUTTON"
    }
}