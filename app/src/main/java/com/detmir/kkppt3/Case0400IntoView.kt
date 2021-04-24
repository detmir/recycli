package com.detmir.kkppt3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.detmir.RecyclerBinderImpl
import com.detmir.kkppt3.viewholders.DigitalOceanViewHolder
import com.detmir.kkppt3.views.*
import com.detmir.recycli.adapters.RecyclerAdapter

class Case0400IntoView : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_0400)
        val recyclerView = findViewById<RecyclerView>(R.id.activity_case_0400_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val recyclerAdapter = RecyclerAdapter(setOf(RecyclerBinderImpl()))
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.bindState(
            listOf(
                CloudItem(
                    id = "GOOGLE",
                    serverName = "Google server",
                    intoView = CloudGoogleItemView::class.java
                ),
                CloudItem(
                    id = "AMAZON",
                    serverName = "Amazon server",
                    intoView = CloudAmazonItemView::class.java
                ),
                CloudItem(
                    id = "AZURE",
                    serverName = "Azure server",
                    intoView = CloudAzureItemView::class.java
                ),
                CloudItem(
                    id = "DIGITAL_OCEAN",
                    serverName = "Digital ocean server",
                    intoView = DigitalOceanViewHolder::class.java
                )
            )
        )
    }
}