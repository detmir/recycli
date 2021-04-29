package framework.screens

import android.view.View
import androidx.test.espresso.NoMatchingViewException
import com.agoda.kakao.common.views.KView
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.text.KButton
import com.detmir.kkppt3.Case0600InfinityActivity
import com.detmir.kkppt3.R
import com.detmir.kkppt3.views.HeaderItemView
import org.hamcrest.Matcher

class Activity600Screen : BaseScreen<Activity600Screen>() {
    override val layoutId: Int = R.layout.activity_case_0600
    override val viewClass: Class<*> = Case0600InfinityActivity::class.java

    class UserRecyclerItem(parent: Matcher<View>) : KRecyclerItem<HeaderItemView>(parent)

    private val recycler = KRecyclerView({ withId(R.id.activity_case_0600_recycler) }, { itemType(::UserRecyclerItem) })

    fun scrollDown() {
        scrollToBottom()
    }

    fun checkUserNameDisplayedHandlingError(userName: String) {
        try {
            checkUserNameDisplayed(userName)
        } catch (e: NoMatchingViewException) {
            pressErrorAndWait()
        }
    }

    private fun pressErrorAndWait() {
        KButton { withId(R.id.bottom_loading_view_button_error) }.click()
        Thread.sleep(3000)
    }

    fun checkUserNameDisplayed(userName: String) {
        recycler.childWith<UserRecyclerItem> { withDescendant { withText(userName) } }.isVisible()
    }

    fun checkBottomProgressDisplayed() {
        KView { withId(R.id.bottom_loading_view_progress) }.isDisplayed()
    }

}