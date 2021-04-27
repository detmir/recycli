package framework.kaspresso.actions

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.Matcher

class ScrollToViewAction(private val areaPercentage: Int = 90) : ViewAction {
    val TAG = ScrollToViewAction::class.java.simpleName

    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isAssignableFrom(View::class.java)
    }

    override fun perform(
        uiController: UiController,
        view: View
    ) {
        if (ViewMatchers.isDisplayingAtLeast(areaPercentage).matches(view)) {
            Log.i(TAG, "View is already displayed.")
            return
        }
        val rect = Rect()
        view.getDrawingRect(rect)
        if (!view.requestRectangleOnScreen(rect, true)) {
            Log.i(TAG, "Scrolling to view was requested, but none of the parents scrolled.")
        }
        uiController.loopMainThreadUntilIdle()
        if (!ViewMatchers.isDisplayingAtLeast(areaPercentage).matches(view)) {
            throw PerformException.Builder()
                .withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(view))
                .withCause(
                    RuntimeException("Scrolling to view was attempted, but the view is not displayed")
                )
                .build()
        }
    }

    override fun getDescription(): String? {
        return "scroll to"
    }
}