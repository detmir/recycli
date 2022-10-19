package com.detmir.kkppt3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.detmir.kkppt3.decorator.HeaderItemDecoration
import com.detmir.kkppt3.views.HeaderItem
import com.detmir.kkppt3.views.UserItem
import com.detmir.recycli.adapters.InfinityState
import com.detmir.recycli.adapters.RecyclerAdapter
import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.ui.bottom.BottomLoading
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Case0800StickyHeaderActivity : AppCompatActivity(), RecyclerAdapter.Callbacks {

    private lateinit var recyclerView: RecyclerView
    private val items = ArrayList<RecyclerItem>()
    private var infiniteItemsErrorThrown = false


    private var recyclerAdapter = RecyclerAdapter(
        binders = setOf(com.detmir.kkppt3.RecyclerBinderImpl(), com.detmir.ui.bottom.RecyclerBinderImpl()),
        infinityCallbacks = this,
        bottomLoading = BottomLoading()
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_0900)

        recyclerView = findViewById<RecyclerView>(R.id.activity_case_0900_recycler)

        val decorator = HeaderItemDecoration(
            parent = recyclerView,
            isHeader = { pos ->
                items[pos] is HeaderItem
            }
        )

        recyclerView.addItemDecoration(decorator)

        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            reverseLayout = false
        }
        recyclerView.adapter = recyclerAdapter
        loadRange(0)
    }

    override fun loadRange(curPage: Int) {
        val delay = if (curPage == 0) 0L else 2000L
        Single.timer(delay, TimeUnit.MILLISECONDS)
            .flatMap {
                val models = mutableListOf<RecyclerItem>()
                models.add(HeaderItem(
                    id = "HEADER_$curPage",
                    title = "HEADER_$curPage",
                    background = android.R.color.white
                ))
                models.addAll((curPage * PAGE_SIZE until (curPage * PAGE_SIZE + PAGE_SIZE)).map {
                    UserItem(
                        id = "USER_$it",
                        firstName = "John $it",
                        online = it < 5
                    ) as RecyclerItem
                })

                Single.just(models)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                if (curPage == 9 && !infiniteItemsErrorThrown) {
                    infiniteItemsErrorThrown = true
                    throw Exception("error")
                }
                it
            }
            .doOnSubscribe {
                recyclerAdapter.bindState(
                    InfinityState(
                        requestState = InfinityState.Request.LOADING,
                        items = items,
                        page = curPage,
                        endReached = curPage == 10
                    )
                )
            }
            .doOnError {
                recyclerAdapter.bindState(
                    InfinityState(
                        requestState = InfinityState.Request.ERROR,
                        items = items,
                        page = curPage,
                        endReached = curPage == 10
                    )
                )
            }
            .doOnSuccess {
                if (curPage == 0) items.clear()
                items.addAll(it)

                recyclerAdapter.bindState(
                    InfinityState(
                        requestState = InfinityState.Request.IDLE,
                        items = items,
                        page = curPage,
                        endReached = curPage == 10
                    )
                )
            }
            .subscribe({}, {})
    }


    companion object {
        const val PAGE_SIZE = 20
    }
}

