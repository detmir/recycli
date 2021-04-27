package framework.kaspresso

import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector

class ScreenScroller {

    private val scrollTimeout = 10_000L

    private fun getUiScrollable(): UiScrollable {
        val uiScrollable = UiScrollable(UiSelector().scrollable(true))
        uiScrollable.swipeDeadZonePercentage = 0.2
        return uiScrollable
    }

    fun scrollForward(): Boolean = try {
        getUiScrollable().scrollForward(30)
    } catch (e: UiObjectNotFoundException) {
        false
    }

    fun scrollBackward(): Boolean = try {
        getUiScrollable().scrollBackward(30)
    } catch (e: UiObjectNotFoundException) {
        false
    }

    fun scrollToBottom() = try {
        val startTime = System.currentTimeMillis()
        while (scrollForward() && System.currentTimeMillis() - startTime <= scrollTimeout) {
        }
    } catch (e: UiObjectNotFoundException) {
        false
    }

}