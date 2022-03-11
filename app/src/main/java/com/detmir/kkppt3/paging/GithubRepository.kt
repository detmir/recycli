package com.detmir.kkppt3.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class GithubRepository {

    fun getSearchResultStream(): Flow<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GithubPagingSource() }
        ).flow
    }


    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}
