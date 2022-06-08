package com.detmir.kkppt3.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.detmir.recycli.adapters.RecyclerItem

abstract class RecycliPagingSource : PagingSource<Int, RecyclerItem>() {

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }

    override fun getRefreshKey(state: PagingState<Int, RecyclerItem>): Int? {
        val closestPageToPosition = state.closestPageToPosition(state.anchorPosition!!)
        val prevKey = closestPageToPosition?.prevKey
        val refreshKey = if (prevKey == null) 0 else prevKey + 1
        Log.d("pager3", "getRefreshKey prevKey=$prevKey refreshKey=$refreshKey")
        return refreshKey
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecyclerItem> {
        val page = params.key ?: 0
        val loadSize = params.loadSize ?: 10
        val from = page * NETWORK_PAGE_SIZE
        val to = page * NETWORK_PAGE_SIZE + loadSize
        Log.d("pager3", "page = $page loadSize = $loadSize from =$from to = $to")
        val items = actualLoad(from, to)

        val nextKey = if (items.isEmpty()) {
            null
        } else {
            // if initial load size = 3 * NETWORK_PAGE_SIZE
            // ensure we're not requesting duplicating items, at the 2nd request
            page + (params.loadSize / NETWORK_PAGE_SIZE)
        }

        Log.d("pager3", "nextKey = $nextKey")
        return LoadResult.Page(
            data = items,
            prevKey = if (page == 0) null else page - 1,
            nextKey = nextKey,
            itemsBefore = from,
            itemsAfter = totalItems - to
        )
    }

    abstract var totalItems: Int
    abstract suspend fun actualLoad(from: Int, to: Int): List<RecyclerItem>
}
