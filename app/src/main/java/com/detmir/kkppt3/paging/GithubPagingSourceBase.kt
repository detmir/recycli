package com.detmir.kkppt3.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.detmir.kkppt3.views.RepoDto
import com.detmir.kkppt3.views.RepoItem
import com.detmir.recycli.adapters.RecyclerItem
import kotlinx.coroutines.delay

class GithubPagingSourceBase(
    private val onPlus: () -> Unit,
    private val getTotal: () -> Int,
    private val cachedData: MutableMap<Int, RepoDto>
) : PagingSource<Int, RecyclerItem>() {

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
        val repos: List<RecyclerItem>
        if (cachedData[from] != null && cachedData[to - 1] != null) {
            Log.d("pager3", "Hit cached")
            repos = cachedData.filter { entry ->
                entry.key in from until to
            }.values.map { repoDto ->
                RepoItem(
                    id = "${repoDto.i}",
                    repoName = repoDto.name,
                    onPlus = onPlus,
                    i = getTotal.invoke()
                )
            }
        } else {
            Log.d("pager3", "Fetch netwirk")
            delay(2000) // emulate network delay

            val repoDtos = (from until to).map { pos ->
                RepoDto(
                    i = pos,
                    name = "my name is $pos"
                )
            }
            repos = repoDtos.map { repoDto ->
                val repoItem = RepoItem(
                    id = "${repoDto.i}",
                    repoName = repoDto.name,
                    onPlus = onPlus,
                    i = getTotal.invoke()
                )
                cachedData[repoDto.i] = repoDto
                repoItem
            }
        }


        val nextKey = if (repos.isEmpty()) {
            null
        } else {
            // if initial load size = 3 * NETWORK_PAGE_SIZE
            // ensure we're not requesting duplicating items, at the 2nd request
            page + (params.loadSize / NETWORK_PAGE_SIZE)
        }

        Log.d("pager3", "nextKey = $nextKey")
        return LoadResult.Page(
            data = repos,
            prevKey = if (page == 0) null else page - 1,
            nextKey = nextKey
        )
    }
}

