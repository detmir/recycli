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

    fun bindState(items: List<RecyclerItem>) {
        super.bindRecyclerItems(
            items = items,
            itemsAtBottom = emptyList(),
            itemsAtTop = emptyList()
        )
    }


    fun bindState(items: List<RecyclerItem>, itemsAtTop: List<RecyclerItem>, itemsAtBottom: List<RecyclerItem>) {
        super.bindRecyclerItems(
            items = items,
            itemsAtTop = emptyList(),
            itemsAtBottom = emptyList()
        )
    }
}