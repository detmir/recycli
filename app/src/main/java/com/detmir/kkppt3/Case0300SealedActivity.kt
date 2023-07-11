package com.detmir.kkppt3

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.detmir.kkppt3.views.ProjectItem
import com.detmir.recycli.adapters.RecyclerAdapter

class Case0300SealedActivity : AppCompatActivity() {
    var toNew = false
    lateinit var recyclerAdapter: RecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_0300)
        val recyclerView = findViewById<RecyclerView>(R.id.activity_case_0300_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = RecyclerAdapter()
        recyclerView.adapter = recyclerAdapter

        updateState()
    }

    fun updateState() {
        recyclerAdapter.bindState(
            listOf(
                ProjectItem.Failed(
                    id = "FAILED",
                    title = "Failed project",
                    why = "",
                    toNew = ::toNew
                ),
                /*ProjectItem.New(
                    id = "NEW",
                    title = "New project",
                    toNew = ::toNew
                ),*/
                ProjectItem.Done.BeforeDeadline(
                    id = "BEFORE_DEAD_LINE",
                    title = "Done before deadline project",
                    toNew = ::toNew
                ),
                ProjectItem.Done.AfterDeadline(
                    id = "AFTER_DEAD_LINE1",
                    title = "Done after 1 deadline project",
                    why = "",
                    toNew = ::toNew
                ),
                ProjectItem.Done.AfterDeadline(
                    id = "AFTER_DEAD_LINE2",
                    title = "Done after 2 deadline project",
                    why = "",
                    toNew = ::toNew
                ),
                ProjectItem.Done.AfterDeadline(
                    id = "AFTER_DEAD_LINE3",
                    title = "Done after 3 deadline project",
                    why = "",
                    toNew = ::toNew
                ),
                ProjectItem.Done.AfterDeadline(
                    id = "AFTER_DEAD_LINE4",
                    title = "Done after 4 deadline project",
                    why = "",
                    toNew = ::toNew
                ),
                ProjectItem.Done.AfterDeadline(
                    id = "AFTER_DEAD_LINE5",
                    title = "Done after 5 deadline project",
                    why = "",
                    toNew = ::toNew
                ),
                ProjectItem.Done.AfterDeadline(
                    id = "AFTER_DEAD_LINE6",
                    title = "Done after 6 deadline project",
                    why = "",
                    toNew = ::toNew
                ),
                if (toNew) {
                    ProjectItem.New(
                        id = "NEW2",
                        title = "New project",
                        toNew = ::toNew
                    )
                } else {
                    ProjectItem.Done.AfterDeadline(
                        id = "AFTER_DEAD_LINE7",
                        title = "Done after 7 deadline project",
                        why = "",
                        toNew = ::toNew
                    )
                }
                ,
                ProjectItem.Done.AfterDeadline(
                    id = "AFTER_DEAD_LINE8",
                    title = "Done after 8 deadline project",
                    why = "",
                    toNew = ::toNew
                ),
            )
        )
    }

    fun toNew() {
        toNew = !toNew
        updateState()
    }
}