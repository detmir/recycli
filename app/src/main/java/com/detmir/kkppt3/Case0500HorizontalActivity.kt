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

class Case0500HorizontalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_0500)
        val recyclerView = findViewById<RecyclerView>(R.id.activity_case_0500_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val recyclerAdapter = RecyclerAdapter(setOf(RecyclerBinderImpl()))
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.bindState(
            listOf(
                HeaderItem(
                    id = "HEADER_SUB_TASKS",
                    title = "Subtasks"
                ),
                SimpleContainerItem(
                    id = "SUB_TASKS_CONTAINER",
                    recyclerState = (0..100).map {
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
    }
}