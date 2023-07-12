package com.detmir.kkppt3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.detmir.kkppt3.views.HeaderItem
import com.detmir.kkppt3.views.UserItem
import com.detmir.recycli.adapters.RecyclerItem
import com.detmir.recycli.adapters.bindState

class Case0200ClickAndStateActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    private val onlineUserNames = mutableListOf("James","Mary","Robert","Patricia")
    private val offlineUserNames = mutableListOf("Michael","Linda","William","Elizabeth","David")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_0200)
        recyclerView = findViewById(R.id.activity_case_0200_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        updateRecycler()
    }


    private fun updateRecycler() {
        val recyclerItems = mutableListOf<RecyclerItem>()

        recyclerItems.add(
            HeaderItem(
                id = "HEADER_ONLINE_OPERATORS",
                title = "Online operators ${onlineUserNames.size}"
            )
        )

        onlineUserNames.forEach { name ->
            recyclerItems.add(
                UserItem(
                    id = name,
                    firstName = name,
                    online = true,
                    onCardClick = ::cardClicked,
                    onMoveToOffline = ::moveToOffline
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
                    onCardClick = ::cardClicked,
                    onMoveToOnline = ::moveToOnline
                )
            )
        }

        recyclerView.bindState(recyclerItems)
    }

    private fun cardClicked(name: String) {
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
    }

    private fun moveToOffline(name: String) {
        onlineUserNames.remove(name)
        offlineUserNames.add(0, name)
        updateRecycler()
    }

    private fun moveToOnline(name: String) {
        offlineUserNames.remove(name)
        onlineUserNames.add(name)
        updateRecycler()
    }
}