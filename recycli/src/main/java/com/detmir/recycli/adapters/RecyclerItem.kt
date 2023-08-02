package com.detmir.recycli.adapters

import androidx.annotation.Keep

@Keep
interface RecyclerItem {
    fun areContentsTheSame(other: RecyclerItem) = this == other
    fun withView(): Class<out Any>? = null
    fun provideId(): String
}