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
import com.facebook.shimmer.ShimmerFrameLayout

@RecyclerItemView
class RepoItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val taskTitle: TextView
    private val taskPos: TextView
    private val taskPlused: TextView
    private val plus: Button
    private val shimmer: ShimmerFrameLayout
    private var state: RepoItem? = null


    init {
        LayoutInflater.from(context).inflate(R.layout.repo_view, this)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        taskTitle = findViewById(R.id.repo_title)
        shimmer = findViewById(R.id.repo_top)
        taskPos = findViewById(R.id.repo_pos)
        taskPlused = findViewById(R.id.repo_plused)
        plus = findViewById(R.id.repo_plus)
        plus.setOnClickListener {
            state?.onPlus?.invoke()
        }
    }

    @RecyclerItemStateBinder
    fun bindState(state: RepoItem) {
        this.state = state
        if (state.placeholder) {
            shimmer.showShimmer(true)
            shimmer.startShimmer()
        } else {
            shimmer.stopShimmer()
            shimmer.hideShimmer()
        }

        taskTitle.text = state.repoName
        taskPos.text = "${state.pos}"
        taskPlused.text = "Plused = ${state.plusedValue}"
    }
}
