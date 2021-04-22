package com.detmir.recycli.adapters

interface RItem {
    fun areContentsTheSame(other: RItem) = this == other
    fun withView(): Class<out Any>? = null
    fun provideId(): String
}