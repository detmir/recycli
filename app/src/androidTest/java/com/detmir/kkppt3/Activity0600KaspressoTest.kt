package com.detmir.kkppt3

import com.agoda.kakao.text.KButton
import framework.screens.Activity600Screen
import org.junit.Test

class Activity0600KaspressoTest : AbstractKaspressoTest() {

    private val pageSize = Case0600InfinityActivity.PAGE_SIZE

    @Test
    fun checkScrollableUsersList() {
        KButton { withId(R.id.activity_main_0600) }.click()

        val lastBatchIndex = 10
        val activity600Screen = Activity600Screen()
        (0..lastBatchIndex).forEach {
            val firstInBatch = pageSize * it
            val lastInBatch = pageSize * it + pageSize - 1

            activity600Screen.checkUserNameDisplayedHandlingError("John $firstInBatch")
            activity600Screen.scrollDown()
            activity600Screen.checkUserNameDisplayed("John $lastInBatch")
            if (it != lastBatchIndex)
                activity600Screen.checkBottomProgressDisplayed()
        }
    }
}