package framework.screens

import android.view.View
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.text.KTextView
import com.detmir.kkppt3.Case0100SimpleActivity
import com.detmir.kkppt3.R
import com.detmir.kkppt3.views.HeaderItemView
import com.detmir.kkppt3.views.UserItemView
import com.kaspersky.kaspresso.screens.KScreen
import framework.screens.BaseScreen.Companion.getText
import org.hamcrest.Matcher

class Activity100Screen : KScreen<Activity100Screen>() {
    override val layoutId: Int = R.layout.activity_case_0100
    override val viewClass: Class<*> = Case0100SimpleActivity::class.java

    class UsersRecyclerItem(parent: Matcher<View>) :
        KRecyclerItem<UserItemView>(parent) {
        val firstName = KTextView(parent) { withId(R.id.user_view_first_name) }
    }

    class HeaderRecyclerItem(parent: Matcher<View>) :
        KRecyclerItem<HeaderItemView>(parent) {
        val headerTitle = KTextView(parent) { withId(R.id.header_view_title) }
    }

    private val userItems = KRecyclerView({ withId(R.id.activity_case_0100_recycler) },
        {
            itemType(::UsersRecyclerItem)
            itemType(::HeaderRecyclerItem)
        }
    )

    fun getUserNames(): List<String> {
        val names = mutableListOf<String>()
        (1..2).forEach { pos ->
            userItems.childAt<UsersRecyclerItem>(pos) {
                names.add(firstName.getText())
            }
        }
        return names
    }

    fun getHeader(): String {
        var header: String? = null
        userItems.firstChild<HeaderRecyclerItem> { header = headerTitle.getText() }
        return header ?: throw AssertionError("Cant get header title")
    }
}