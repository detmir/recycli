package framework.kaspresso.interceptors

import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast
import com.kaspersky.kaspresso.autoscroll.AutoScrollProvider
import com.kaspersky.kaspresso.logger.UiTestLogger
import com.kaspersky.kaspresso.logger.UiTestLoggerImpl
import framework.kaspresso.ScreenScroller
import framework.kaspresso.actions.ScrollToViewAction

class AutoScrollNotVisibleViewProviderImpl(
    private val timeoutMs: Long = 10_000L,
    private val areaPercentage: Int = 90
) : AutoScrollProvider<ViewInteraction> {

    private val logger: UiTestLogger = UiTestLoggerImpl("AutoScrollNotVisibleViewProviderImpl")
    private val screenScroller = ScreenScroller()

    @Throws(Throwable::class)
    override fun <T> withAutoScroll(interaction: ViewInteraction, action: () -> T): T {
        return try {
            action.invoke()
        } catch (error: Throwable) {
            return scroll(interaction, action, error)
        }
    }

    @Throws(Throwable::class)
    override fun <T> scroll(
        interaction: ViewInteraction,
        action: () -> T,
        cachedError: Throwable
    ): T {
        var isDisplayed = false
        var startTime = System.currentTimeMillis()

        do {
            try {
                interaction.check(matches(isDisplayingAtLeast(1)))
                interaction.perform(ScrollToViewAction(areaPercentage))
                interaction.check(matches(isDisplayingAtLeast(areaPercentage)))
                isDisplayed = true
                break
            } catch (error: Throwable) {
            }
        } while (screenScroller.scrollForward() && System.currentTimeMillis() - startTime <= timeoutMs)

        startTime = System.currentTimeMillis()
        if (!isDisplayed) {
            do {
                try {
                    interaction.check(matches(isDisplayingAtLeast(1)))
                    interaction.perform(ScrollToViewAction(areaPercentage))
                    interaction.check(matches(isDisplayingAtLeast(areaPercentage)))
                    isDisplayed = true
                    break
                } catch (error: Throwable) {
                }
            } while (screenScroller.scrollBackward() && System.currentTimeMillis() - startTime <= timeoutMs)
        }

        if (isDisplayed) logger.i("View autoScroll successfully performed.")

        return try {
            action.invoke()
        } catch (error: Throwable) {
            throw cachedError
        }
    }
}