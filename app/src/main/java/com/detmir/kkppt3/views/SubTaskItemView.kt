package com.detmir.kkppt3.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.detmir.kkppt3.R
import com.detmir.recycli.annotations.RecyclerStateBinder
import com.detmir.recycli.annotations.RecyclerStateView
import toPx

@RecyclerStateView
class SubTaskItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val taskTitle: TextView
    private val taskDescription: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.sub_task_view, this)
        layoutParams = ViewGroup.LayoutParams(180.toPx, ViewGroup.LayoutParams.WRAP_CONTENT)
        taskTitle = findViewById(R.id.sub_task_title)
        taskDescription = findViewById(R.id.sub_task_description)
    }

    @RecyclerStateBinder
    fun bindState(subTaskItem: SubTaskItem) {
        taskTitle.text = subTaskItem.title
        taskDescription.text = subTaskItem.description
    }
}