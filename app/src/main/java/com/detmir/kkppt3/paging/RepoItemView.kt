package com.detmir.kkppt3.paging

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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

    init {
        LayoutInflater.from(context).inflate(R.layout.repo_view, this)
        taskTitle = findViewById(R.id.repo_title)
        taskDescription = findViewById(R.id.repo_description)
    }

    @RecyclerItemStateBinder
    fun bindState(repo: Repo) {
        taskTitle.text = repo.repoName
        taskDescription.text = repo.id
    }
}
