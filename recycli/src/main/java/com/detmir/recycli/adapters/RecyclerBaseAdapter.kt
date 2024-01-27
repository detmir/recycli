package com.detmir.recycli.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

@Keep
open class RecyclerBaseAdapter(
    val getRecyclerItem: (pos: Int) -> RecyclerItem
) {
    companion object {
        var warmedUp = false
        val stateToBindersWrapped: HashMap<String, BinderWrapped> = HashMap()
        val bindersToStateWrapped: HashMap<Int, BinderWrapped> = HashMap()
        private const val RECYCLI_ASSETS_PATH = "recycli"
    }

    private fun listAssetFiles(context: Context): List<String> {
        return try {
            context.assets?.list(RECYCLI_ASSETS_PATH)?.toList() ?: emptyList()
        } catch (e: IOException) {
            emptyList()
        }
    }

    fun warmUpBinders(context: Context) {
        if (!warmedUp) {
            val kspBinders = mutableListOf<RecyclerBinder>()
            for (packCamel in listAssetFiles(context)) {
                val pack = packCamel.replace("_", ".")
                try {
                    val clazz = Class.forName("$pack.RecyclerBinderImpl")
                    val binder = clazz.newInstance() as RecyclerBinder
                    kspBinders.add(binder)
                } catch (e: Throwable) {
                    // log here
                }
            }

            var i = 1
            for (recyclerBinder in kspBinders) {
                for (entr in recyclerBinder.stateToIndexMap) {
                    val binderWrapped = BinderWrapped(
                        bindersPosition = i,
                        wrappedBinderType = i * 1000000 + entr.value,
                        binder = recyclerBinder,
                        type = entr.value
                    )
                    stateToBindersWrapped[entr.key] = binderWrapped
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