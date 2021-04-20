package com.detmir.kkppt3

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.detmir.kkppt3.views.*
import com.detmir.recycli.adapters.RecyclerAdapter
import com.detmir.recycli.adapters.RecyclerAdapterRegular
import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.adapters.RecyclerStateRegular
import toPx

class Case02ClickAndStateActivity : AppCompatActivity() {

    lateinit var recyclerAdapterRegular: RecyclerAdapterRegular
    lateinit var recyclerView: RecyclerView

    private val onlineUserNames = mutableListOf(
        "James",
        "Mary",
        "Robert",
        "Patricia"
    )

    private val offlineUserNames = mutableListOf(
        "Michael",
        "Linda",
        "William",
        "Elizabeth",
        "David",
        "Barbara",
        "Richard"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RecyclerAdapter.staticBinders = setOf(
            com.detmir.ui.RecyclerBinderImpl(),
            com.detmir.shapes.RecyclerBinderImpl(),
            com.detmir.RecyclerBinderImpl()
        )
        setContentView(R.layout.activity_case_02)


        // Common recycler initialization
        recyclerView = findViewById(R.id.activity_case_02_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                when (view) {
                    is HeaderView -> outRect.set(24.toPx, 32.toPx, 24.toPx, 8.toPx)
                    is UserView -> outRect.set(
                        0.toPx,
                        0.toPx,
                        0.toPx,
                        0.toPx
                    ) //we have paddings inside view
                }
            }
        })


        recyclerAdapterRegular = RecyclerAdapterRegular()
        recyclerView.adapter = recyclerAdapterRegular


        //Create Recycli state and populate RecyclerView
        updateRecycler()
    }


    private fun updateRecycler() {
        val recyclerItems = mutableListOf<RecyclerItem>()

        recyclerItems.add(
            Header(
                id = "HEADER_TASKS",
                title = "Tasks"
            )
        )

        recyclerItems.add(
            BigTask(
                id = "TASK",
                title = "This is task title",
                description = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English."
            )
        )


        recyclerItems.add(
            Header(
                id = "HEADER_ONLINE_OPERATORS",
                title = "Online operators ${onlineUserNames.size}"
            )
        )

        onlineUserNames.forEach {
            recyclerItems.add(
                User(
                    id = it,
                    firstName = it,
                    online = true,
                    onMoveToOffline = {
                        onlineUserNames.remove(it)
                        offlineUserNames.add(0, it)
                        updateRecycler()
                    }
                )
            )
        }

        recyclerItems.add(
            Header(
                id = "HEADER_OFFLINE_OPERATORS",
                title = "Offline operators ${offlineUserNames.size}"
            )
        )

        offlineUserNames.forEach {
            recyclerItems.add(
                User(
                    id = it,
                    firstName = it,
                    online = false,
                    onMoveToOnline = {
                        offlineUserNames.remove(it)
                        onlineUserNames.add(it)
                        updateRecycler()
                    }
                )
            )
        }


        val recyclerState = RecyclerStateRegular(
            items = recyclerItems
        )

        recyclerAdapterRegular.bindState(recyclerState)
    }
}