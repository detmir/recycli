package com.detmir.kkppt3

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

class ScrollToPositionAction(val pos: Int): ViewAction {
    override fun getDescription(): String {
        return "scroll RecyclerView to position"
    }

    override fun getConstraints(): Matcher<View> {
        return allOf<View>(isAssignableFrom(RecyclerView::class.java), isDisplayed())
    }

    override fun perform(uiController: UiController?, view: View?) {
        val recyclerView = view as RecyclerView
        val itemCount = recyclerView.adapter?.itemCount ?: 0

        val position = if (pos < itemCount) {
            pos
        } else {
            0
        }
        recyclerView.scrollToPosition(position)
        uiController?.loopMainThreadUntilIdle()
    }
}