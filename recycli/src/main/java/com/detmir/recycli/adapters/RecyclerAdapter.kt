package com.detmir.recycli.adapters

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.detmir.recycli.annotations.RecyclerBinderAdapter

open class RecyclerAdapter(
    binders: Set<RecyclerBinder>? = null,
    private val infinityCallbacks: Callbacks? = null,
    private val bottomLoading: RecyclerBottomLoading? = null,
    private val infinityType: InfinityType = InfinityType.SCROLL
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        var staticBinders: Set<RecyclerBinder>?
            set(value) {
                RecyclerBaseAdapter.staticBinders = value
            }
            get() {
                return RecyclerBaseAdapter.staticBinders
            }
    }

    var recyclerView: RecyclerView? = null

    var attachListeners: Map<String, AttachListener>? = null
    var firstAppearanceListeners: Map<String, FirstAppearanceListener>? = null

    private val items = mutableListOf<RecyclerItem>()
    private val itemsAtTop = mutableListOf<RecyclerItem>()
    private val itemsAtBottom = mutableListOf<RecyclerItem>()
    private val combinedItems = mutableListOf<RecyclerItem>()

    private var scrollChecker: Runnable = Runnable { checkNeedLoad() }
    private var infinityState: InfinityState? = null
    val recyclerBaseAdapter: RecyclerBaseAdapter

    init {

//        val d = RecyclerBinderAdapter::class
//        Log.d("dasd","$d")

        recyclerBaseAdapter = RecyclerBaseAdapter(
            getRecyclerItem = { pos ->
                combinedItems[pos]
            },
            binders = binders
        )
    }


    fun bindState(items: List<RecyclerItem>) {
        if (infinityCallbacks != null) {
            throw Exception("You are trying to bind non infinity state on infinity adapter")
        }
        bindRecyclerItems(
            items = items
        )
    }

    fun bindState(infinityState: InfinityState) {
        if (infinityCallbacks == null) {
            throw Exception("You are trying to bind infinity state on non infinity adapter")
        }
        this.infinityState = infinityState
        bindRecyclerItems(infinityState.items)
    }

    @Suppress("unused")
    fun bindAction(action: RecyclerAction?) {
        when (action) {
            is RecyclerAction.ScrollToItem -> {
                val pos = this.combinedItems.indexOf(action.recyclerItem)
                scrollToPos(pos)
            }
            is RecyclerAction.ScrollToTop -> {
                scrollToPos(0, action.smooth)
            }
            else -> {}
        }
    }


    private fun bindRecyclerItems(items: List<RecyclerItem>) {
        val oldCombined = mutableListOf<RecyclerItem>().apply { addAll(combinedItems) }
        this.itemsAtBottom.clear()
        this.itemsAtTop.clear()
        this.items.clear()
        this.items.addAll(items)

        postProcess()

        commitItems()
        val diffResult =
            DiffUtil.calculateDiff(RecyclerDiffCallback(oldCombined, combinedItems))
        diffResult.dispatchUpdatesTo(this)

        if (recyclerView?.adapter == null) {
            recyclerView?.adapter = this
        }
    }

    @Suppress("unused")
    fun addFirstAppearanceListeners(firstAppearanceListeners: Map<String, FirstAppearanceListener>) {
        this.firstAppearanceListeners = firstAppearanceListeners.toMap()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = recyclerBaseAdapter.onCreateViewHolder(parent, viewType)

    override fun getItemViewType(position: Int): Int = recyclerBaseAdapter.getItemViewType(position)

    fun getItem(position: Int): RecyclerItem = combinedItems[position]

    override fun getItemCount(): Int = combinedItems.size

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) = recyclerBaseAdapter.onBindViewHolder(holder, position, payloads)

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) = recyclerBaseAdapter.onBindViewHolder(holder, position)

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        attachListeners?.let {
            val id = getId(holder)
            if (id != null) {
                it[id]?.onViewDetachedFromWindow()
            }
        }
        super.onViewDetachedFromWindow(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        attachListeners?.let {
            val id = getId(holder)
            if (id != null) {
                it[id]?.onViewAttachedToWindow()
            }
        }
        super.onViewAttachedToWindow(holder)
    }

    private fun getId(holder: RecyclerView.ViewHolder): String? {
        val pos = recyclerView?.getChildAdapterPosition(holder.itemView) ?: 0
        return if (pos >= 0 && pos < combinedItems.size) {
            combinedItems[pos].provideId()
        } else {
            null
        }
    }

    private fun commitItems() {
        combinedItems.clear()
        combinedItems.addAll(itemsAtTop)
        combinedItems.addAll(items)
        combinedItems.addAll(itemsAtBottom)
    }

    private fun getScroller(pos: Int): SmoothScroller {
        val smoothScroller: SmoothScroller = object : LinearSmoothScroller(recyclerView?.context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = pos
        return smoothScroller
    }

    private fun scrollToPos(pos: Int, smooth: Boolean = true) {
        recyclerView?.post {
            if (recyclerView?.isAttachedToWindow == true) {
                actuallyDoScroll(pos, smooth)
            }
        }
        //We need to duplicate scroll in case of recycler animator animating changes
        recyclerView?.postDelayed({
            if (recyclerView?.isAttachedToWindow == true) {
                actuallyDoScroll(pos, smooth)
            }
        }, 300)
    }

    private fun actuallyDoScroll(pos: Int, smooth: Boolean) {
        val count = recyclerView?.adapter?.itemCount ?: 0
        if (pos in 0 until count) {
            // We have crash with flexlayoutmanager
            // "Tmp detached view should be removed from RecyclerView before it can be recycled"
            // So double check
            try {
                recyclerView?.layoutManager?.run {
                    if (smooth) {
                        startSmoothScroll(getScroller(pos))
                    } else {
                        scrollToPosition(pos)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }


    private fun postProcess() {
        val recyclerStateInfinity = this.infinityState
        if (recyclerStateInfinity != null) {
            when (recyclerStateInfinity.requestState) {
                //LOADING
                InfinityState.Request.LOADING -> {
                    if (recyclerStateInfinity.page > 0 && recyclerStateInfinity.items.isNotEmpty()) {
                        bottomLoading?.provideProgress()?.let {
                            this.itemsAtBottom.add(it)
                        }
                    }

                }

                //ERRORS
                InfinityState.Request.ERROR -> {
                    if (recyclerStateInfinity.page > 0 && recyclerStateInfinity.items.isNotEmpty()) {
                        bottomLoading?.provideError(reload = {
                            infinityCallbacks?.loadRange((recyclerStateInfinity.page))
                        })?.let {
                            itemsAtBottom.add(it)
                        }
                    }
                }

                //Idle
                InfinityState.Request.IDLE -> {
                    when {
                        isInfiniteByButton() && recyclerStateInfinity.items.isNotEmpty() && !recyclerStateInfinity.endReached -> {
                            bottomLoading?.provideButton(next = {
                                infinityCallbacks?.loadRange((recyclerStateInfinity.page) + 1)
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


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView

        if (isInfinity()) {
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
            if (lm.stackFromEnd) {
                val firstVisibleItemPosition = lm.findFirstVisibleItemPosition()
                if (firstVisibleItemPosition < 5) {
                    rangeLoading()
                }
            } else {
                val firstVisibleItemPosition = lm.findFirstVisibleItemPosition()
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount - 5) {
                    rangeLoading()
                }
            }
        }
    }

    private fun rangeLoading() {
        var nextPage = getCurrentPage()
        if (!isLoading()) {
            if (!isLoadingError()) {
                nextPage++
            }
            infinityCallbacks?.loadRange(nextPage)
        }
    }

    private fun getCurrentPage(): Int {
        return infinityState?.page ?: 0
    }

    private fun isLoading(): Boolean {
        return infinityState?.requestState == InfinityState.Request.LOADING
    }

    private fun isEndReached(): Boolean {
        return infinityState?.endReached == true
    }

    private fun isLoadingError(): Boolean {
        return infinityState?.requestState == InfinityState.Request.ERROR
    }


    private fun isInfiniteByButton() = isInfinity() && infinityType == InfinityType.BUTTON

    private fun isInfiniteByScroll() = isInfinity() && infinityType == InfinityType.SCROLL

    private fun isInfinity() = infinityCallbacks != null


    interface AttachListener {
        fun onViewAttachedToWindow()
        fun onViewDetachedFromWindow()
    }

    interface FirstAppearanceListener {
        fun onFirstAppearance(recyclerItem: RecyclerItem)
    }

    data class BinderWrapped(
        val wrappedBinderType: Int,
        val bindersPosition: Int,
        val binder: RecyclerBinder,
        val type: Int
    )

    interface Callbacks {
        fun loadRange(curPage: Int)
    }


    enum class InfinityType {
        SCROLL, BUTTON
    }
}