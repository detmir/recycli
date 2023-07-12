package com.detmir.kkppt3.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.detmir.kkppt3.R
import com.detmir.kkppt3.RecyclerBinderImpl
import com.detmir.recycli.adapters.RecyclerAdapter
import com.detmir.recycli.adapters.bindState
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView

@RecyclerItemView
class SimpleContainerItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val recyclerView: RecyclerView

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.simple_recycler_conteiner_view, this, true)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            300
        )

        recyclerView = view.findViewById(R.id.simple_recycler_container_recycler)

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    @RecyclerItemStateBinder
    fun bindState(state: SimpleContainerItem) {
        recyclerView.bindState(state.recyclerState)
    }
}
