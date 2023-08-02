package com.detmir.recycli.adapters

import androidx.annotation.Keep

@Keep
sealed class RecyclerAction {
    data class ScrollToTop(val smooth: Boolean = true) : RecyclerAction()
    data class ScrollToItem(val recyclerItem: RecyclerItem) : RecyclerAction()
}
