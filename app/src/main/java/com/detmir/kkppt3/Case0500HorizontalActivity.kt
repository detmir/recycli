package com.detmir.kkppt3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.detmir.kkppt3.views.BigTaskItem
import com.detmir.kkppt3.views.HeaderItem
import com.detmir.kkppt3.views.SimpleContainerItem
import com.detmir.kkppt3.views.SubTaskItem
import com.detmir.recycli.adapters.RecyclerAdapter
import com.detmir.recycli.adapters.bindState

class Case0500HorizontalActivity : AppCompatActivity() {

    var i = 0
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_0500)
        recyclerView = findViewById(R.id.activity_case_0500_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        handleState()
    }

    private fun handleState() {
        i++
        recyclerView.bindState(
            listOf(
                HeaderItem(
                    id = "HEADER_SUB_TASKS",
                    title = "Subtasks $i"
                ),
                SimpleContainerItem(
                    id = "SUB_TASKS_CONTAINER",
                    recyclerState = (0..10).map {
                        SubTaskItem(
                            id = "SUB_TASK_$it",
                            title = "Sub task $it",
                            description = "It is a long established fact that a reader will be distracted by the readable content"
                        )
                    }
                ),
                BigTaskItem(
                    id = "TASK",
                    title = "The second task title",
                    description = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English."
                )
            )
        )
        recyclerView.postDelayed({
            handleState()
        }, 5000)
    }
}