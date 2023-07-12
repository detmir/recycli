package com.detmir.recycli.adapters

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.bindState(items: List<RecyclerItem>) {
    if (this.adapter == null) {
        val recyclerAdapterRegular = RecyclerAdapter()
        this.adapter = recyclerAdapterRegular
    }
    (this.adapter as? RecyclerAdapter)?.bindState(items)
}

fun RecyclerView.bindState(infinityState: InfinityState) {
    if (this.adapter == null) {
        val recyclerAdapterRegular = RecyclerAdapter()
        this.adapter = recyclerAdapterRegular
    }
    (this.adapter as? RecyclerAdapter)?.bindState(infinityState)
}

fun RecyclerView.setInfinityCallbacks(callbacks: RecyclerAdapter.Callbacks) {
    if (this.adapter == null) {
        val recyclerAdapterRegular = RecyclerAdapter()
        recyclerAdapterRegular.infinityCallbacks = callbacks
        this.adapter = recyclerAdapterRegular
    } else {
        (this.adapter as? RecyclerAdapter)?.infinityCallbacks = callbacks
    }
}

fun RecyclerView.setBottomLoading(recyclerBottomLoading: RecyclerBottomLoading) {
    if (this.adapter == null) {
        val recyclerAdapterRegular = RecyclerAdapter()
        recyclerAdapterRegular.bottomLoading = recyclerBottomLoading
        this.adapter = recyclerAdapterRegular
    } else {
        (this.adapter as? RecyclerAdapter)?.bottomLoading = recyclerBottomLoading
    }

}

fun RecyclerView.setInfinityType(infinityType: RecyclerAdapter.InfinityType) {
    if (this.adapter == null) {
        val recyclerAdapterRegular = RecyclerAdapter()
        recyclerAdapterRegular.infinityType = infinityType
        this.adapter = recyclerAdapterRegular
    } else {
        (this.adapter as? RecyclerAdapter)?.infinityType = infinityType
    }
}