package com.detmir.ui.toast

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.annotations.RecyclerItemState
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView
import com.detmir.ui.R
import toPx

@RecyclerItemView
class ToastView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var message: TextView
    lateinit var state: State

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        inflater.inflate(R.layout.toast_view, this, true)
        setPadding(24.toPx, 32.toPx, 24.toPx, 8.toPx)
        message = findViewById(R.id.toast_message)
    }

    @RecyclerItemStateBinder
    fun bindState(state: State) {
        this.state = state
        message.text = state.message
    }

    @RecyclerItemState
    data class State(
        val id: String,
        val message: String
    ) : RecyclerItem {
        override fun provideId(): String = id
    }
}