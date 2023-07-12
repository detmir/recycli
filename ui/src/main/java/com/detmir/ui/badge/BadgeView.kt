package com.detmir.ui.badge

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView
import com.detmir.ui.R
import toPx

@RecyclerItemView
class BadgeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var message: TextView
    lateinit var state: BadgeItem

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        inflater.inflate(R.layout.badge_view, this, true)
        setPadding(24.toPx, 32.toPx, 24.toPx, 8.toPx)
        message = findViewById(R.id.badge_message)
    }

    @RecyclerItemStateBinder
    fun bindState(state: BadgeItem) {
        this.state = state
        message.text = state.message
    }
}