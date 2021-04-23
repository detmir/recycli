package com.detmir.kkppt3.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.detmir.kkppt3.R
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView

@RecyclerItemView
class UserItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val firstName: TextView
    private val toOnlineButton: TextView
    private val toOfflineButton: TextView
    private val status: FrameLayout
    private val holder: FrameLayout


    private var userItem: UserItem? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.user_view, this)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        clipToPadding = false
        firstName = findViewById(R.id.user_view_first_name)
        status = findViewById(R.id.user_view_status)
        holder = findViewById(R.id.user_view_status_holder)
        toOnlineButton = findViewById(R.id.user_view_to_online)
        toOfflineButton = findViewById(R.id.user_view_to_offline)

        toOnlineButton.setOnClickListener {
            userItem?.onMoveToOnline?.invoke(userItem?.firstName ?: "")
        }

        toOfflineButton.setOnClickListener {
            userItem?.onMoveToOffline?.invoke(userItem?.firstName ?: "")
        }

        holder.setOnClickListener {
            userItem?.onCardClick?.invoke(userItem?.firstName ?: "")
        }
    }

    @RecyclerItemStateBinder
    fun bindState(userItem: UserItem) {
        this.userItem = userItem
        firstName.text = userItem.firstName
        @ColorRes val color = if (userItem.online) R.color.recycliGreen else R.color.recycliRed
        status.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, color))

        toOfflineButton.isVisible = userItem.onMoveToOffline != null && userItem.online
        toOnlineButton.isVisible = userItem.onMoveToOnline != null && !userItem.online
    }
}