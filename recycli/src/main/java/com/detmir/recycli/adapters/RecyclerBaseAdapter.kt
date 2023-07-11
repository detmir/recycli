package com.detmir.recycli.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

open class RecyclerBaseAdapter(
    val getRecyclerItem: (pos: Int) -> RecyclerItem
) {
    companion object {
        var warmedUp = false
        var kspBinders: Set<RecyclerBinder>? = null
        val stateToBindersWrapped: HashMap<String, BinderWrapped> = HashMap()
        val bindersToStateWrapped: HashMap<Int, BinderWrapped> = HashMap()
    }

    init {

    }

    private fun listAssetFiles(context: Context): List<String> {
        try {
            return context.assets?.list("recycli")?.toList() ?: emptyList()
        } catch (e: IOException) {
            return emptyList()
        }
    }

    fun warmUpBinders(context: Context) {
        if (!warmedUp) {
            val kspBinders = listAssetFiles(context).map { packCamel ->
                packCamel.replace("_",".")
            }.map { pack ->
                Class.forName("$pack.RecyclerBinderImpl")
            }.map { clazz ->
                clazz.newInstance() as RecyclerBinder
            }


            var i = 1
            kspBinders.forEach { recyclerBinder ->
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
            warmedUp = true
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
}