package com.detmir.kkppt3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.detmir.kkppt3.viewholders.ServerItem
import com.detmir.kkppt3.views.*
import com.detmir.recycli.adapters.RecyclerAdapter

class Case0101SimpleVHActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_0101)
        val recyclerView = findViewById<RecyclerView>(R.id.activity_case_0101_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val recyclerAdapter = RecyclerAdapter(setOf(RecyclerBinderImpl()))
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.bindState(
            listOf(
                HeaderItem(
                    id = "HEADER_SERVERS",
                    title = "Servers"
                ),
                ServerItem(
                    id = "SERVER1",
                    serverAddress = "124.45.22.12"
                ),
                ServerItem(
                    id = "SERVER2",
                    serverAddress = "90.0.0.28"
                )
            )
        )
    }
}