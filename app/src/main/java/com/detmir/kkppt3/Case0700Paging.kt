package com.detmir.kkppt3

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.detmir.kkppt3.paging.GithubRepository
import com.detmir.kkppt3.paging.ReposAdapter
import com.detmir.kkppt3.views.UserItem
import com.detmir.recycli.adapters.InfinityState
import com.detmir.recycli.adapters.RecyclerAdapter
import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.ui.bottom.BottomLoading
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class Case0700Paging : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: ReposAdapter


    private lateinit var butt: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        val cs = CoroutineScope(Dispatchers.Default)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_0700)
        recyclerView = findViewById<RecyclerView>(R.id.activity_case_0700_recycler)
        butt = findViewById<Button>(R.id.activity_case_0700_refresh)


        butt.setOnClickListener {
            adapter.refresh()
        }

        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            reverseLayout = false
        }

        adapter = ReposAdapter()
        recyclerView.adapter = adapter
        val githubRepository = GithubRepository()

        adapter.onPagesUpdatedFlow
        cs.launch {
            githubRepository.getSearchResultStream().collect { pagindData ->
                adapter.submitData(pagindData)
            }
        }

    }



    companion object {
        const val PAGE_SIZE = 20
    }
}

