package com.detmir.kkppt3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.detmir.kkppt3.views.BigTaskItem
import com.detmir.kkppt3.views.HeaderItem
import com.detmir.kkppt3.views.KeepPosContainerItem
import com.detmir.kkppt3.views.SubTaskItem
import com.detmir.kkppt3.views.UserItem
import com.detmir.recycli.adapters.RecyclerAdapter
import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.adapters.bindState
import com.detmir.ui.badge.BadgeItem
import com.detmir.ui.toast.ToastView

class Case0000DemoActivity : AppCompatActivity() {

    lateinit var recyclerAdapterRegular: RecyclerAdapter
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
        setContentView(R.layout.activity_case_0000)

        recyclerView = findViewById(R.id.activity_case_0000_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Create Recycli state and populate RecyclerView
        updateRecycler()
    }


    private fun updateRecycler() {
        val recyclerItems = mutableListOf<RecyclerItem>()

        recyclerItems.add(
            ToastView.State(
                id = "TOAST",
                message = "I'm toast"
            )
        )

        recyclerItems.add(
            BadgeItem(
                id = "BADGE",
                message = "I'm badge"
            )
        )

        recyclerItems.add(
            HeaderItem(
                id = "HEADER_TASKS",
                title = "Tasks"
            )
        )

        recyclerItems.add(
            BigTaskItem(
                id = "TASK",
                title = "This is task title",
                description = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English."
            )
        )


        recyclerItems.add(
            BigTaskItem(
                id = "TASK",
                title = "The second task title",
                description = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English."
            )
        )


        recyclerItems.add(
            HeaderItem(
                id = "HEADER_SUBSTASKS",
                title = "Subtasks"
            )
        )


        recyclerItems.add(
            KeepPosContainerItem(
                id = "SUBTASKS_CONTAINER",
                recyclerState = (0..100).map {
                    SubTaskItem(
                        id = "SUBTASK_$it",
                        title = "Sub task $it",
                        description = "It is a long established fact that a reader will be distracted by the readable content"
                    )
                }
            )
        )


        recyclerItems.add(
            HeaderItem(
                id = "HEADER_ONLINE_OPERATORS",
                title = "Online operators ${onlineUserNames.size}"
            )
        )

        onlineUserNames.forEach {
            recyclerItems.add(
                UserItem(
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
            HeaderItem(
                id = "HEADER_OFFLINE_OPERATORS",
                title = "Offline operators ${offlineUserNames.size}"
            )
        )

        offlineUserNames.forEach {
            recyclerItems.add(
                UserItem(
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

        recyclerView.bindState(recyclerItems)
    }
}