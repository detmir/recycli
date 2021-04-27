package framework.kaspresso

import com.kaspersky.kaspresso.idlewaiting.KautomatorWaitForIdleSettings
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import framework.kaspresso.interceptors.AutoScrollNotVisibleViewBehaviorInterceptor
import java.util.*

class KaspressoBuilder {

    fun default(): Kaspresso.Builder {
        return Kaspresso.Builder.simple().apply {
            flakySafetyParams.timeoutMs = 4_000L
            flakySafetyParams.intervalMs = 100L
            language.switchInApp(Locale("ru", "RU"))
            kautomatorWaitForIdleSettings = KautomatorWaitForIdleSettings.boost()
            viewBehaviorInterceptors.add(AutoScrollNotVisibleViewBehaviorInterceptor(8_000L, 95))
        }
    }
}