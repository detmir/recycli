package com.detmir.recycli.adapters

data class RecyclerStateRegular(
    override val items: List<RecyclerItem> = listOf(),
    override val itemsAtTop: List<RecyclerItem> = listOf(),
    override val itemsAtBottom: List<RecyclerItem> = listOf()
) : RecyclerStateBase {

    @Suppress("unused")
    fun toIdle(
        items: List<RecyclerItem>? = null,
        itemsAtTop: List<RecyclerItem>? = null,
        itemsAtBottom: List<RecyclerItem>? = null
    ): RecyclerStateRegular = this.copy(
        items = items ?: this.items,
        itemsAtTop = itemsAtTop ?: this.itemsAtTop,
        itemsAtBottom = itemsAtBottom ?: this.itemsAtBottom
    )
}
