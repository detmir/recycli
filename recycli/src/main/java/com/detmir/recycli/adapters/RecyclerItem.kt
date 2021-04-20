package com.detmir.recycli.adapters

import android.view.View

interface RecyclerItem : RecyclerIdProvider {
    fun areContentsTheSame(other: RecyclerItem) = this == other
    fun withView(): Class<out Any>? = null
}