package com.detmir.recycli.adapters

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.detmir.recycli.adapters.RecyclerAdapter

class RecyclerAdapterInfinity(
    binders: Set<RecyclerBinder>? = null,
    val callbacks: Callbacks,
    val bottomLoading: RecyclerBottomLoading? = null,
    private val type: Type = Type.SCROLL
) : RecyclerAdapter(
    binders = binders
) {
    private var scrollChecker: Runnable = Runnable { checkNeedLoad() }
    private var recyclerStateInfinity: RecyclerStateInfinity? = null

    override fun postProcess() {
        val recyclerStateInfinity = this.recyclerStateInfinity
        if (recyclerStateInfinity != null) {
            when (recyclerStateInfinity.requestState) {
                //LOADING
                RecyclerStateInfinity.Request.LOADING -> {
                    if (recyclerStateInfinity.page > 0 && recyclerStateInfinity.items.isNotEmpty()) {
                        bottomLoading?.provideProgress()?.let {
                            this.itemsAtBottom.add(it)
                        }
                    }

                }

                //ERRORS
                RecyclerStateInfinity.Request.ERROR -> {
                    if (recyclerStateInfinity.page > 0 && recyclerStateInfinity.items.isNotEmpty()) {
                        bottomLoading?.provideError(reload = {
                            callbacks.loadRange((recyclerStateInfinity.page))
                        })?.let {
                            itemsAtBottom.add(it)
                        }
                    }
                }

                //Idle
                RecyclerStateInfinity.Request.IDLE -> {
                    when {
                        isInfiniteByButton() && recyclerStateInfinity.items.isNotEmpty() && !recyclerStateInfinity.endReached -> {
                            bottomLoading?.provideButton(next = {
                                callbacks.loadRange((recyclerStateInfinity.page) + 1)
                            })?.let {
                                this.itemsAtBottom.add(it)
                            }
                        }

                        isInfiniteByScroll() && recyclerStateInfinity.items.isNotEmpty() && !recyclerStateInfinity.endReached -> {
                            bottomLoading?.provideDummy()?.let {
                                this.itemsAtBottom.add(it)
                            }
                        }
                    }
                }

            }
        }
    }

    fun bindState(recyclerStateInfinity: RecyclerStateInfinity) {
        this.recyclerStateInfinity = recyclerStateInfinity
        super.bindState(recyclerStateInfinity)
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!isLoadingError() && !isLoading()) {
                    tryInfinity()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!isLoadingError() && !isLoading()) {
                    tryInfinity()
                }
            }
        })
    }


    private fun tryInfinity() {
        recyclerView?.removeCallbacks { scrollChecker }
        recyclerView?.post(scrollChecker)
    }

    private fun checkNeedLoad() {
        if (!isLoading() && !isEndReached()) {
            val lm = recyclerView?.layoutManager as LinearLayoutManager
            val visibleItemCount = lm.childCount
            val totalItemCount = lm.itemCount
            val firstVisibleItemPosition = lm.findFirstVisibleItemPosition()
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount - 5) {
                rangeLoading()
            }
        }
    }

    private fun rangeLoading() {
        var nextPage = getCurrentPage()
        if (!isLoading()) {
            if (!isLoadingError()) {
                nextPage++
            }
            callbacks.loadRange(nextPage)
        }
    }

    private fun getCurrentPage(): Int {
        return recyclerStateInfinity?.page ?: 0
    }

    private fun isLoading(): Boolean {
        return recyclerStateInfinity?.requestState == RecyclerStateInfinity.Request.LOADING
    }

    private fun isEndReached(): Boolean {
        return recyclerStateInfinity?.endReached == true
    }

    private fun isLoadingError(): Boolean {
        return recyclerStateInfinity?.requestState == RecyclerStateInfinity.Request.ERROR
    }


    private fun isInfiniteByButton() = type == Type.BUTTON

    private fun isInfiniteByScroll() = type == Type.SCROLL

    interface Callbacks {
        fun loadRange(curPage: Int)
    }


    enum class Type {
        SCROLL, BUTTON
    }
}