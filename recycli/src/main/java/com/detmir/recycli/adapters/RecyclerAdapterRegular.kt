package com.detmir.recycli.adapters

import com.detmir.recycli.adapters.RecyclerAdapter

class RecyclerAdapterRegular(
    binders: Set<RecyclerBinder>? = null
) : RecyclerAdapter(
    binders = binders
) {
    fun bindState(stateRegular: RecyclerStateRegular) {
        super.bindState(stateRegular)
    }
}