package com.detmir.recycli.adapters

data class InfinityState (
    val items: List<RecyclerItem> = listOf(),
    val page: Int = 0,
    val endReached: Boolean = false,
    val requestState: Request = Request.IDLE
) {
    enum class Request {
        IDLE, LOADING, ERROR
    }

    @Suppress("unused")
    fun toPageLoading(page: Int? = null): InfinityState = this.copy(
        page = page ?: this.page,
        requestState = Request.LOADING
    )

    @Suppress("unused")
    fun toPageError(
        page: Int? = null,
        items: List<RecyclerItem>? = null
    ): InfinityState = this.copy(
        page = page ?: this.page,
        items = items ?: this.items,
        requestState = Request.ERROR
    )

    @Suppress("unused")
    fun toIdle(
        items: List<RecyclerItem>? = null,
        endReached: Boolean? = null,
        page: Int? = null
    ): InfinityState = this.copy(
        items = items ?: this.items,
        endReached = endReached ?: this.endReached,
        requestState = Request.IDLE,
        page = page ?: this.page
    )
}
