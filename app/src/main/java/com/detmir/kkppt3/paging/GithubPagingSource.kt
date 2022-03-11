package com.detmir.kkppt3.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.detmir.kkppt3.paging.GithubRepository.Companion.NETWORK_PAGE_SIZE
import kotlinx.coroutines.delay

class GithubPagingSource: PagingSource<Int, Repo>() {
    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        delay(2000)
        val page = params.key ?: 0
        val loadSize = params.loadSize ?: 10
        val repos = ((page * NETWORK_PAGE_SIZE) until (page * NETWORK_PAGE_SIZE + loadSize)).map {
            Repo(
                id = "$it",
                repoName = "repo $it"
            )
        }
        val nextKey = if (repos.isEmpty()) {
            null
        } else {
            // initial load size = 3 * NETWORK_PAGE_SIZE
            // ensure we're not requesting duplicating items, at the 2nd request
            page + (params.loadSize / NETWORK_PAGE_SIZE)
        }
        Log.d("adas","ad")
        return LoadResult.Page(
            data = repos,
            prevKey = if (page == 0) null else page - 1,
            nextKey = nextKey
        )
    }
}
