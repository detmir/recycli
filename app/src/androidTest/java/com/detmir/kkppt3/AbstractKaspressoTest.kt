package com.detmir.kkppt3

import androidx.test.rule.ActivityTestRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import framework.kaspresso.KaspressoBuilder
import org.junit.Before
import org.junit.Rule

abstract class AbstractKaspressoTest : TestCase(KaspressoBuilder().default()) {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Before
    fun startMainActivity() {
        activityTestRule.launchActivity(null)
    }

}