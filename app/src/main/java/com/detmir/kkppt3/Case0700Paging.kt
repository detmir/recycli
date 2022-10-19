package com.detmir.kkppt3

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.detmir.kkppt3.paging.RecycliPagingAdapter
import com.detmir.kkppt3.paging.RecycliPagingSource
import com.detmir.kkppt3.views.RepoDto
import com.detmir.kkppt3.views.RepoItem
import com.detmir.recycli.adapters.RecyclerBaseAdapter
import com.detmir.recycli.adapters.RecyclerItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Case0700Paging : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var plusedValue = 0
    private var cachedData = mutableMapOf<Int, RepoDto>()
    private lateinit var adapter: RecycliPagingAdapter
    private var githubPagingSource: RecycliPagingSource? = null
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        val cs = CoroutineScope(Dispatchers.Default)
        RecyclerBaseAdapter.staticBinders = setOf(
            com.detmir.ui.bottom.RecyclerBinderImpl(),
            RecyclerBinderImpl()
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_0700)
        recyclerView = findViewById(R.id.activity_case_0700_recycler)
        swipeRefresh = findViewById(R.id.activity_case_0700_swipe)

        swipeRefresh.isRefreshing = true

        swipeRefresh.setOnRefreshListener {
            plusedValue = 0
            cachedData.clear()
            adapter.refresh()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        adapter = RecycliPagingAdapter(
            placeHolderProvider = ::placeHolderProvider
        )
        recyclerView.adapter = adapter

        cs.launch {
            Pager(
                config = PagingConfig(
                    pageSize = RecycliPagingSource.NETWORK_PAGE_SIZE,
                    prefetchDistance = 2,
                    enablePlaceholders = true,
                    initialLoadSize = RecycliPagingSource.NETWORK_PAGE_SIZE
                ),
                pagingSourceFactory = {
                    val githubPagingSourceLocal = object : RecycliPagingSource() {
                        override var totalItems: Int = 1000
                        override suspend fun actualLoad(from: Int, to: Int): List<RecyclerItem> {
                            return load(from, to)
                        }
                    }
                    githubPagingSource = githubPagingSourceLocal
                    githubPagingSourceLocal
                }
            ).flow.collect { pagindData ->
                adapter.submitData(pagindData)
            }
        }

    }

    private fun placeHolderProvider(pos: Int): RecyclerItem {
        return RepoItem(
            id = "$pos",
            repoName = "",
            pos = pos,
            plusedValue = 0,
            placeholder = true,
            onPlus = null
        )
    }


    private fun onPlus() {
        Log.d("pager3", "onPlus plusedValue=$plusedValue")
        plusedValue++
        githubPagingSource?.invalidate()
    }


    private fun mapItem(repoDto: RepoDto): RepoItem {
        return RepoItem(
            id = "${repoDto.i}",
            repoName = repoDto.name,
            onPlus = ::onPlus,
            pos = repoDto.i,
            plusedValue = plusedValue,
            placeholder = false
        )
    }


    private suspend fun load(from: Int, to: Int): List<RecyclerItem> {
        val items: List<RecyclerItem>
        if (cachedData[from] != null && cachedData[to - 1] != null) {
            Log.d("pager3", "Fetch cache")
            items = cachedData.filter { entry ->
                entry.key in from until to
            }.values.map(::mapItem)
        } else {
            Log.d("pager3", "Fetch network")
            delay(2000) // emulate network delay
            val repoDtos = (from until to).map { pos ->
                val repoDto = RepoDto(
                    i = pos,
                    name = "my name is $pos"
                )
                cachedData[repoDto.i] = repoDto
                repoDto
            }

            swipeRefresh.isRefreshing = false

            items = repoDtos
                .map(::mapItem)

        }
        return items
    }
}

