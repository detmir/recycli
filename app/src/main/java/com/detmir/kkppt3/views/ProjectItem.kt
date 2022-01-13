package com.detmir.kkppt3.views

import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState

@RecyclerItemState
sealed class ProjectItem : RecyclerItem {
    abstract val id: String
    abstract val title: String
    abstract val toNew: () -> Unit

    data class Failed(
        override val id: String,
        override val title: String,
        override val toNew: () -> Unit,
        val why: String
    ) : ProjectItem()

    data class New(
        override val id: String,
        override val title: String,
        override val toNew: () -> Unit,
    ) : ProjectItem()

    sealed class Done : ProjectItem() {
        data class BeforeDeadline(
            override val id: String,
            override val title: String,
            override val toNew: () -> Unit,
        ) : Done()

        data class AfterDeadline(
            override val id: String,
            override val title: String,
            override val toNew: () -> Unit,
            val why: String
        ) : Done()
    }

    override fun provideId() = id
}
