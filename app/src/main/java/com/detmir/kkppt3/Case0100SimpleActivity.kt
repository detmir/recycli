package com.detmir.kkppt3

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.detmir.RecyclerBinderImpl
import com.detmir.kkppt3.views.*
import com.detmir.recycli.adapters.RecyclerAdapter
import com.detmir.recycli.adapters.RecyclerAdapterRegular
import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.adapters.RecyclerStateRegular
import toPx

class Case0100SimpleActivity : AppCompatActivity() {

    private val recyclerAdapterRegular = RecyclerAdapterRegular(setOf(RecyclerBinderImpl()))
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_case_0100)

        // Common recycler initialization
        recyclerView = findViewById(R.id.activity_case_0100_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerAdapterRegular

        recyclerAdapterRegular.bindState(
            listOf(
                HeaderItem(
                    id = "HEADER_TASKS",
                    title = "Tasks"
                ),
                BigTaskItem(
                    id = "TASK",
                    title = "This is task title",
                    description = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English."
                ),
                UserItem(
                    id = "USER",
                    firstName = "Andrew",
                    online = true
                )

            )
        )
    }
}