package com.detmir.recycli.adapters

import androidx.annotation.Keep

@Keep
interface RecyclerBottomLoading {
    fun provideProgress(): RecyclerItem
    fun provideDummy(): RecyclerItem
    fun provideError(reload: (() -> Unit)): RecyclerItem
    fun provideButton(next: (() -> Unit)): RecyclerItem
}