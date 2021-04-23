package com.detmir.kkppt3.views

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

@RecyclerItemState
sealed class ProjectItem : RecyclerItem {
    abstract val id: String
    abstract val title: String

    data class Failed(
        override val id: String,
        override val title: String,
        val why: String
    ) : ProjectItem()

    data class New(
        override val id: String,
        override val title: String
    ) : ProjectItem()

    sealed class Done: ProjectItem() {
        data class BeforeDeadline(
            override val id: String,
            override val title: String
        ) : Done()

        data class AfterDeadline(
            override val id: String,
            override val title: String,
            val why: String
        ) : Done()
    }

    override fun provideId() = id
}
