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
class BigTaskItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val taskTitle: TextView
    private val taskDescription: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.big_task_view, this)
        taskTitle = findViewById(R.id.big_task_title)
        taskDescription = findViewById(R.id.big_task_description)
    }

    @RecyclerStateBinder
    fun bindState(bigTaskItem: BigTaskItem) {
        taskTitle.text = bigTaskItem.title
        taskDescription.text = bigTaskItem.description
    }
}