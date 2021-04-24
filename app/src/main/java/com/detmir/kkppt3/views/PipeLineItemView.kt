package com.detmir.kkppt3.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.detmir.kkppt3.R
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView

@RecyclerItemView
class PipeLineItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val destination: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.pipeline_item_view, this)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        destination = findViewById(R.id.pipeline_item_destination)
    }

    @RecyclerItemStateBinder
    fun bindState(input: PipeLineItem.Input) {
        destination.text = input.from
    }


    @RecyclerItemStateBinder
    fun bindState(output: PipeLineItem.Output) {
        destination.text = output.to
    }
}