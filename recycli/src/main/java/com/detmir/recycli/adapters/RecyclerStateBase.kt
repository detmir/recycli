package com.detmir.recycli.adapters

interface RecyclerStateBase {
    val items: List<RecyclerItem>
    val itemsAtTop: List<RecyclerItem>
    val itemsAtBottom: List<RecyclerItem>
}