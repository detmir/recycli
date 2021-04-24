package com.detmir.kkppt3.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.detmir.kkppt3.R
import com.detmir.recycli.adapters.RecyclerAdapter
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView

@RecyclerItemView
class KeepPosContainerItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val recycler: RecyclerView
    private val recyclerPagedAdapter: RecyclerAdapter
    private var llm: LinearLayoutManager? = null
    private var state: KeepPosContainerItem? = null

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.recycler_conteiner_view, this, true)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        recyclerPagedAdapter = RecyclerAdapter()
        recycler = view.findViewById(R.id.recycler_container_recycler)

        recycler.run {
            isNestedScrollingEnabled = false

            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = recyclerPagedAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == SCROLL_STATE_IDLE) {
                        savePosition()
                    }
                }
            })
        }
    }

    @RecyclerItemStateBinder
    fun bindState(state: KeepPosContainerItem) {


        recyclerPagedAdapter.bindState(state.recyclerState)
        val keepPos = state.scrollKeeper.pos
        val keepOffset = state.scrollKeeper.offset
        llm?.scrollToPositionWithOffset(keepPos ?: 0, keepOffset ?: 0)
    }

    private fun savePosition() {
        val pos = llm?.findFirstVisibleItemPosition()
        val v = pos?.let { llm?.findViewByPosition(pos) }
        var offset = v?.x?.toInt() ?: 0

        var parentPaddingLeft = 0
        v?.parent?.let {
            if (it is ViewGroup) {
                parentPaddingLeft = it.paddingLeft

            }
        }
        offset -= parentPaddingLeft
        state?.scrollKeeper?.pos = pos
        state?.scrollKeeper?.offset = offset
    }


    override fun onDetachedFromWindow() {
        savePosition()
        super.onDetachedFromWindow()
    }

}