package com.detmir.kkppt3

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.detmir.kkppt3.paging.GithubPagingSource
import com.detmir.kkppt3.paging.Paging3RecycliAdapter
import com.detmir.kkppt3.views.RepoDto
import com.detmir.kkppt3.views.RepoItem
import com.detmir.recycli.adapters.RecyclerBaseAdapter
import com.detmir.recycli.adapters.RecyclerItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Case0700Paging : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var total = 0
    private var cachedData = mutableMapOf<Int, RepoDto>()
    private lateinit var adapter: Paging3RecycliAdapter
    private var githubPagingSource = GithubPagingSource(
        externalLoad = ::externalLoad
    )

    private lateinit var refresh: Button
    private lateinit var invalidate: Button

    fun getTotal() = total

    override fun onCreate(savedInstanceState: Bundle?) {
        val cs = CoroutineScope(Dispatchers.Default)
        RecyclerBaseAdapter.staticBinders = setOf(
            com.detmir.ui.RecyclerBinderImpl(),
            com.detmir.kkppt3.RecyclerBinderImpl()
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_0700)
        recyclerView = findViewById<RecyclerView>(R.id.activity_case_0700_recycler)
        refresh = findViewById<Button>(R.id.activity_case_0700_refresh)
        invalidate = findViewById<Button>(R.id.activity_case_0700_invalidate)

        refresh.setOnClickListener {
            adapter.refresh()
        }

        invalidate.setOnClickListener {
                githubPagingSource.invalidate()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        adapter = Paging3RecycliAdapter()

        recyclerView.adapter = adapter

        cs.launch {
            Pager(
                config = PagingConfig(
                    pageSize = GithubPagingSource.NETWORK_PAGE_SIZE,
                    prefetchDistance = 2,
                    enablePlaceholders = false,
                    initialLoadSize = GithubPagingSource.NETWORK_PAGE_SIZE
                ),
                pagingSourceFactory = ::provideGithubPagingSource
            ).flow.collect { pagindData ->
                adapter.submitData(pagindData)
            }
        }

    }


    fun provideGithubPagingSource(): GithubPagingSource {
        githubPagingSource = GithubPagingSource(
            externalLoad = ::externalLoad
        )
        return githubPagingSource
    }


    fun onPlus() {
        Log.d("pager3","onPlus total=$total")
        total++
        githubPagingSource.invalidate()
    }


    private suspend fun externalLoad(from: Int, to: Int): List<RecyclerItem> {
        val repos: List<RecyclerItem>
        if (cachedData[from] != null && cachedData[to - 1] != null) {
            Log.d("pager3", "Hit cached")
            repos = cachedData.filter { entry ->
                entry.key in from until to
            }.values.map { repoDto ->
                RepoItem(
                    id = "${repoDto.i}",
                    repoName = repoDto.name,
                    onPlus = ::onPlus,
                    i = total
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
                    onPlus = ::onPlus,
                    i = total
                )
                cachedData[repoDto.i] = repoDto
                repoItem
            }
        }
        return repos
    }


    companion object {
        const val PAGE_SIZE = 20
    }
}

