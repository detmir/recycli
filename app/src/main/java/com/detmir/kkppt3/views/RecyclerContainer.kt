package com.detmir.kkppt3.views

import androidx.annotation.ColorInt
import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.adapters.RecyclerStateRegular
import com.detmir.recycli.adapters.ScrollKeeper
import com.detmir.recycli.annotations.RecyclerState

@RecyclerState
data class RecyclerContainer(
    val id: String,
    val scrollKeeper: ScrollKeeper = ScrollKeeper(),
    val recyclerState: RecyclerStateRegular
): RecyclerItem {
    override fun provideId(): String {
        return id
    }
}