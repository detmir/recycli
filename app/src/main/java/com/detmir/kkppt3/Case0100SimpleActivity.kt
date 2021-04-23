package com.detmir.kkppt3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.detmir.RecyclerBinderImpl
import com.detmir.kkppt3.views.*
import com.detmir.recycli.adapters.RecyclerAdapter

class Case0100SimpleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_0100)
        val recyclerView = findViewById<RecyclerView>(R.id.activity_case_0100_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val recyclerAdapter = RecyclerAdapter(setOf(RecyclerBinderImpl()))
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.bindState(
            listOf(
                HeaderItem(
                    id = "HEADER_USERS",
                    title = "Users"
                ),
                UserItem(
                    id = "USER_ANDREW",
                    firstName = "Andrew",
                    online = true
                ),
                UserItem(
                    id = "USER_MAX",
                    firstName = "Max",
                    online = true
                )
            )
        )
    }
}