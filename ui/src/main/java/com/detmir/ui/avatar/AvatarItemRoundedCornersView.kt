package com.detmir.ui.avatar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import com.detmir.recycli.annotations.RecyclerStateBinder
import com.detmir.recycli.annotations.RecyclerStateView

@RecyclerStateView
class AvatarItemRoundedCornersView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        layoutParams = ViewGroup.LayoutParams(100,100)
    }

    @RecyclerStateBinder
    fun bindState(state: AvatarItem.State) {
        setBackgroundColor(Color.parseColor("#ff00ffff"))
        text = "bind Avatar Rounded Corners ${state.text}"
    }

}