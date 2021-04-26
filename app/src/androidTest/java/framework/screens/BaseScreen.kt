package framework.screens

import android.view.View
import android.widget.TextView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import com.agoda.kakao.common.actions.BaseActions
import com.kaspersky.kaspresso.screens.KScreen
import framework.kaspresso.ScreenScroller

abstract class BaseScreen<out T : KScreen<T>> : KScreen<T>() {

    companion object {
        private val screenScroller = ScreenScroller()
        fun scrollForward() = screenScroller.scrollForward()
        fun scrollToBottom() {
            screenScroller.scrollToBottom()
        }

        fun BaseActions.getText(): String {
            var text = ""

            view.perform(object : ViewAction {
                override fun getConstraints() = isAssignableFrom(TextView::class.java)

                override fun getDescription() = "Get text from view"

                override fun perform(uiController: UiController, view: View) {
                    val textView = view as TextView
                    text = textView.text.toString()
                }
            })

            return text
        }

    }
}