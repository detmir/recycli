package com.detmir.recycli.adapters

interface RecyclerBottomLoading {
    fun provideProgress(): RecyclerItem
    fun provideDummy(): RecyclerItem
    fun provideError(reload: (() -> Unit)): RecyclerItem
    fun provideButton(next: (() -> Unit)): RecyclerItem
}