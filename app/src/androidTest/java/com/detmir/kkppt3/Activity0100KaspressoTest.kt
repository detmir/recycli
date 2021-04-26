package com.detmir.kkppt3

import com.agoda.kakao.text.KButton
import framework.screens.Activity100Screen
import org.assertj.core.api.Assertions
import org.junit.Test

class Activity0100KaspressoTest : AbstractKaspressoTest() {

    @Test
    fun checkAllItems() {
        KButton { withId(R.id.activity_main_0100) }.click()

        Assertions.assertThat(Activity100Screen().getHeader()).isEqualTo("Users")
        Assertions.assertThat(Activity100Screen().getUserNames()).isEqualTo(listOf("Andrew", "Max"))
    }
}