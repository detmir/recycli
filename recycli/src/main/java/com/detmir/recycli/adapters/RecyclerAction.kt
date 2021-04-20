package com.detmir.recycli.adapters

sealed class RecyclerAction {
    data class ScrollToTop(val smooth: Boolean = true) : RecyclerAction()
    data class ScrollToItem(val recyclerItem: RecyclerItem) : RecyclerAction()
}
