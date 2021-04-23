package com.detmir.kkppt3.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.detmir.kkppt3.R
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView

@RecyclerItemView
class ProjectItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val projectTitle: TextView
    private val projectDescription: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.project_item_view, this)
        projectTitle = findViewById(R.id.project_item_title)
        projectDescription = findViewById(R.id.project_item_description)
    }

    @RecyclerItemStateBinder
    fun bindState(projectItem: ProjectItem) {
        projectTitle.text = projectItem.title
        when (projectItem) {
            is ProjectItem.Failed -> projectDescription.text = "Failed"
            is ProjectItem.New -> projectDescription.text = "New"
            is ProjectItem.Done.AfterDeadline -> projectDescription.text = "After deadline"
            is ProjectItem.Done.BeforeDeadline -> projectDescription.text = "Before deadline"
        }
    }
}