package com.detmir.recycli.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller

abstract class RecyclerAdapter(
    binders: Set<RecyclerBinder>? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var recyclerView: RecyclerView? = null
    protected val itemsAtBottom = mutableListOf<RecyclerItem>()

    private val attachListeners: Map<String, AttachListener>? = null
    private val itemsAtTop = mutableListOf<RecyclerItem>()
    private val items = mutableListOf<RecyclerItem>()
    private val combinedItems = mutableListOf<RecyclerItem>()
    private val stateToBindersWrapped: HashMap<String, BinderWrapped> = HashMap()
    private val bindersToStateWrapped: HashMap<Int, BinderWrapped> = HashMap()
    private var firstAppearanceListeners: Map<String, FirstAppearanceListener>? = null

    init {
        val realBinders = binders ?:  staticBinders
        ?: throw Exception("No binder found. Please pass Recylii binder to the constructor or via staticBinders")

        var i = 1
        realBinders.forEach { recyclerBinder ->
            recyclerBinder.stateToIndexMap.forEach {
                val binderWrapped = BinderWrapped(
                    bindersPosition = i,
                    wrappedBinderType = i * 1000000 + it.value,
                    binder = recyclerBinder,
                    type = it.value
                )
                stateToBindersWrapped[it.key] = binderWrapped
                bindersToStateWrapped[binderWrapped.wrappedBinderType] = binderWrapped
            }
            i++
        }
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
        }
    }

    open fun postProcess() {}

    protected fun bindState(state: RecyclerStateBase) {
        val oldCombined = mutableListOf<RecyclerItem>().apply { addAll(combinedItems) }

        this.itemsAtBottom.clear()
        this.itemsAtBottom.addAll(state.itemsAtBottom)
        this.itemsAtTop.clear()
        this.itemsAtTop.addAll(state.itemsAtTop)
        this.items.clear()
        this.items.addAll(state.items)

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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binderWrapped = bindersToStateWrapped[viewType]
        if (binderWrapped != null) {
            return binderWrapped.binder.onCreateViewHolder(parent, binderWrapped.type)
        } else {
            throw Exception("Cant find binder for a viewType=$viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val recyclerItem = getItem(position)
        val state = provideStateWithView(recyclerItem)
        val binderWrapped = stateToBindersWrapped[state]
        //if (binderWrapped == null) throw Exception("No view found for state=$state")
        //val binderWrapped = stateToBindersWrapped[state]!!
        return binderWrapped?.wrappedBinderType ?: throw Exception("No view found for state=$state")
    }


    override fun getItemCount(): Int = combinedItems.size

    fun getItem(position: Int): RecyclerItem = combinedItems[position]

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        onBindViewHolder(holder, position)
    }

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recyclerItem = getItem(position)
        firstAppearanceListeners?.let {
            val id = recyclerItem.provideId()
            if (id.isNotEmpty()) {
                it[id]?.onFirstAppearance(recyclerItem)
            }
        }
        val state: String = provideStateWithView(recyclerItem)
        val binderWrapped = stateToBindersWrapped[state]!!
        binderWrapped.binder.onBindViewHolder(holder, position, state, recyclerItem)
    }

    private fun getId(holder: RecyclerView.ViewHolder): String? {
        val pos = recyclerView?.getChildAdapterPosition(holder.itemView) ?: 0
        return if (pos >= 0 && pos < combinedItems.size) {
            combinedItems[pos].provideId()
        } else {
            null
        }
    }

    private fun provideStateWithView(recyclerItem: RecyclerItem): String {
        val className = recyclerItem.javaClass.canonicalName!!
        return if (recyclerItem.withView() == null) {
            "$className#default"
        } else {
            "$className#${recyclerItem.withView()!!.canonicalName}"
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


    interface AttachListener {
        fun onViewAttachedToWindow()
        fun onViewDetachedFromWindow()
    }

    interface FirstAppearanceListener {
        fun onFirstAppearance(recyclerItem: RecyclerItem)
    }

    protected data class BinderWrapped(
        val wrappedBinderType: Int,
        val bindersPosition: Int,
        val binder: RecyclerBinder,
        val type: Int
    )

    companion object {
        var staticBinders: Set<RecyclerBinder>? = null
    }
}