package com.detmir.kkppt3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.detmir.RecyclerBinderImpl
import com.detmir.kkppt3.views.PipeLineItem
import com.detmir.kkppt3.views.ProjectItem
import com.detmir.recycli.adapters.RecyclerAdapter

class Case0301SealedSeveralBinds : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_0301)
        val recyclerView = findViewById<RecyclerView>(R.id.activity_case_0301_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val recyclerAdapter = RecyclerAdapter(setOf(RecyclerBinderImpl()))
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.bindState(
            listOf(
                PipeLineItem.Output(
                    id = "OUTPUT",
                    to = "Output server"
                ),
                PipeLineItem.Input(
                    id = "INPUT1",
                    from = "Input server"
                ),
                PipeLineItem.Input(
                    id = "INPUT2",
                    from = "Input server 2"
                )
            )
        )
    }
}