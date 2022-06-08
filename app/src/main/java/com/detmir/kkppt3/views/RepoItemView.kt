package com.detmir.kkppt3.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.detmir.kkppt3.R
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView

@RecyclerItemView
class RepoItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val taskTitle: TextView
    private val taskDescription: TextView
    private val plus: Button
    private var state: RepoItem? = null


    init {
        LayoutInflater.from(context).inflate(R.layout.repo_view, this)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        taskTitle = findViewById(R.id.repo_title)
        taskDescription = findViewById(R.id.repo_description)
        plus = findViewById(R.id.repo_plus)
        plus.setOnClickListener {
            state?.onPlus?.invoke()
        }
    }

    @RecyclerItemStateBinder
    fun bindState(state: RepoItem) {
        this.state = state
        taskTitle.text = state.repoName
        taskDescription.text = "${state.i}"
    }
}
