package com.detmir.kkppt3.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.detmir.kkppt3.R
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView
import toPx

@RecyclerItemView
class HeaderItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val title: TextView

    init {
        Log.d("a","init Header sdasd")
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        LayoutInflater.from(context).inflate(R.layout.header_view, this)

        setPadding(24.toPx, 32.toPx, 24.toPx, 8.toPx)
        title = findViewById(R.id.header_view_title)
    }

    @RecyclerItemStateBinder
    fun bindState(headerItem: HeaderItem) {
        setBackgroundColor(ContextCompat.getColor(context, headerItem.background))
        title.text = headerItem.title
        tag = headerItem.id
    }
}