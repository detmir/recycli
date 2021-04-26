package com.detmir.kkppt3

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class Activity0100Test {

    @get:Rule
    val activityRule = ActivityScenarioRule(Case0100SimpleActivity::class.java)

    @Test
    fun checkAllItems() {
        onView(isRoot()).perform(waitFor(5000))
        onView(withId(R.id.activity_case_0100_recycler)).check(matches(isDisplayed()))
        onView(withTagValue(Matchers.`is`("HEADER_USERS"))).check(matches(isDisplayed()))
        onView(withTagValue(Matchers.`is`("USER_MAX"))).check(matches(isDisplayed()))
        onView(withTagValue(Matchers.`is`("USER_ANDREW"))).check(matches(isDisplayed()))
    }
}


