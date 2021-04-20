package com.detmir.kkppt3.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.detmir.kkppt3.R
import com.detmir.recycli.annotations.RecyclerStateBinder
import com.detmir.recycli.annotations.RecyclerStateView

@RecyclerStateView
class HeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val title: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.header_view, this)
        title = findViewById(R.id.header_view_title)
    }

    @RecyclerStateBinder
    fun bindState(header: Header) {
        title.text = header.title
    }
}