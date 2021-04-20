package com.detmir.recycli.adapters

data class RecyclerStateInfinity(
    override val items: List<RecyclerItem> = listOf(),
    override val itemsAtTop: List<RecyclerItem> = listOf(),
    override val itemsAtBottom: List<RecyclerItem> = listOf(),
    val page: Int = 0,
    val endReached: Boolean = false,
    val requestState: Request = Request.IDLE
) : RecyclerStateBase {
    enum class Request {
        IDLE, LOADING, ERROR
    }

    @Suppress("unused")
    fun toPageLoading(page: Int? = null): RecyclerStateInfinity = this.copy(
        page = page ?: this.page,
        requestState = Request.LOADING
    )

    @Suppress("unused")
    fun toPageError(
        page: Int? = null,
        items: List<RecyclerItem>? = null
    ): RecyclerStateInfinity = this.copy(
        page = page ?: this.page,
        items = items ?: this.items,
        requestState = Request.ERROR
    )

    @Suppress("unused")
    fun toIdle(
        items: List<RecyclerItem>? = null,
        itemsAtTop: List<RecyclerItem>? = null,
        endReached: Boolean? = null,
        page: Int? = null
    ): RecyclerStateInfinity = this.copy(
        items = items ?: this.items,
        itemsAtTop = itemsAtTop ?: this.itemsAtTop,
        endReached = endReached ?: this.endReached,
        requestState = Request.IDLE,
        page = page ?: this.page
    )
}
