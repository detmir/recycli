package com.detmir.recycli.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller

open class RecyclerBaseAdapter(
    val getRecyclerItem: (pos: Int) -> RecyclerItem,
    binders: Set<RecyclerBinder>? = null,
) {

    private val stateToBindersWrapped: HashMap<String, BinderWrapped> = HashMap()
    private val bindersToStateWrapped: HashMap<Int, BinderWrapped> = HashMap()

    init {
        val realBinders = binders ?: staticBinders
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

    fun onCreateViewHolder(
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

    fun getItemViewType(position: Int): Int {
        val recyclerItem = getRecyclerItem(position)
        val state = provideStateWithView(recyclerItem)
        val binderWrapped = stateToBindersWrapped[state]
        return binderWrapped?.wrappedBinderType ?: throw Exception("No view found for state=$state")
    }

    fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        onBindViewHolder(holder, position)
    }


    fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recyclerItem = getRecyclerItem(position)
        val state: String = provideStateWithView(recyclerItem)
        val binderWrapped = stateToBindersWrapped[state]!!
        binderWrapped.binder.onBindViewHolder(holder, position, state, recyclerItem)
    }


    private fun provideStateWithView(recyclerItem: RecyclerItem): String {
        val className = recyclerItem.javaClass.canonicalName!!
        return if (recyclerItem.withView() == null) {
            "$className#default"
        } else {
            "$className#${recyclerItem.withView()!!.canonicalName}"
        }
    }

    data class BinderWrapped(
        val wrappedBinderType: Int,
        val bindersPosition: Int,
        val binder: RecyclerBinder,
        val type: Int
    )

    companion object {
        var staticBinders: Set<RecyclerBinder>? = null
    }
}