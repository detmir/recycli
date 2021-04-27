package framework.kaspresso.interceptors

import androidx.test.espresso.ViewInteraction
import com.kaspersky.kaspresso.interceptors.behavior.ViewBehaviorInterceptor

class AutoScrollNotVisibleViewBehaviorInterceptor(
    timeoutMs: Long,
    areaPercentage: Int
) : ViewBehaviorInterceptor {

    private val autoScrollProvider = AutoScrollNotVisibleViewProviderImpl(timeoutMs, areaPercentage)


    override fun <T> intercept(interaction: ViewInteraction, action: () -> T) =
        autoScrollProvider.withAutoScroll(interaction, action)

}