package com.detmir.kkppt3

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.detmir.kkppt3.Case0600InfinityActivity.Companion.PAGE_SIZE
import com.detmir.ui.bottom.BottomLoading
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class Activity0600Test {

    @get:Rule
    val activityRule = ActivityScenarioRule(Case0600InfinityActivity::class.java)

    @Test
    fun checkAllItems() {
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.activity_case_0600_recycler)).check(matches(isDisplayed()))
        (0..10).forEach {
            gotoPage(
                page = it,
                withError = it == 3,
                lastPage = it == 10
            )
        }
        onView(isRoot()).perform(waitFor(1000))
    }


    private fun gotoPage(page: Int, withError: Boolean = false, lastPage: Boolean = false) {
        val user1 = PAGE_SIZE * page + 1
        val user2 = PAGE_SIZE * page + 2
        onView(isRoot()).perform(waitFor(2000))
        onView(withId(R.id.activity_case_0600_recycler)).perform(ScrollToPositionAction(PAGE_SIZE * page + 2))
        onView(withTagValue(Matchers.`is`("USER_$user1"))).check(matches(isDisplayed()))
        onView(withTagValue(Matchers.`is`("USER_$user2"))).check(matches(isDisplayed()))
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.activity_case_0600_recycler)).perform(ScrollToBottomAction())

        if (!lastPage) {
            onView(isRoot()).perform(waitFor(1000))
            onView(withTagValue(Matchers.`is`(BottomLoading.BOTTOM_PROGRESS))).check(matches(isDisplayed()))
        }

        if (withError) {
            onView(isRoot()).perform(waitFor(5000))
            onView(withTagValue(Matchers.`is`(BottomLoading.BOTTOM_ERROR))).check(matches(isDisplayed()))
            onView(withTagValue(Matchers.`is`(BottomLoading.BOTTOM_ERROR))).perform(click())
            onView(isRoot()).perform(waitFor(1000))
            onView(withTagValue(Matchers.`is`(BottomLoading.BOTTOM_PROGRESS))).check(matches(isDisplayed()))
        }
    }
}


