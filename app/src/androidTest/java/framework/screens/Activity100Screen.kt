package framework.screens

import android.view.View
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.text.KTextView
import com.detmir.kkppt3.Case0100SimpleActivity
import com.detmir.kkppt3.R
import com.kaspersky.kaspresso.screens.KScreen
import framework.screens.BaseScreen.Companion.getText
import org.hamcrest.Matcher

class Activity100Screen : KScreen<Activity100Screen>() {
    override val layoutId: Int = R.layout.activity_case_0100
    override val viewClass: Class<*> = Case0100SimpleActivity::class.java

    class UsersRecyclerItem(parent: Matcher<View>) :
        KRecyclerItem<UsersRecyclerItem>(parent) {
        val firstName = KTextView(parent) { withId(R.id.user_view_first_name) }
    }

    private val usersRecyclerItem = KRecyclerView({ withId(R.id.activity_case_0100_recycler) },
        { itemType(Activity100Screen::UsersRecyclerItem) })

    fun getUserNames(): List<String> {
        val names = mutableListOf<String>()

        val usersAmount = usersRecyclerItem.getSize() - 1
        for (index in 1..usersAmount) {
            usersRecyclerItem.childAt<UsersRecyclerItem>(index) {
                names.add(firstName.getText())
            }
        }

        return names
    }

    class HeaderRecyclerItem(parent: Matcher<View>) :
        KRecyclerItem<UsersRecyclerItem>(parent) {
        val headerTitle = KTextView(parent) { withId(R.id.header_view_title) }
    }

    private val headerRecyclerItem = KRecyclerView({ withId(R.id.activity_case_0100_recycler) },
        { itemType(Activity100Screen::HeaderRecyclerItem) })

    fun getHeader(): String {
        var header: String? = null
        headerRecyclerItem.firstChild<HeaderRecyclerItem> { header = headerTitle.getText() }
        return header ?: throw AssertionError("Cant get header title")
    }

}