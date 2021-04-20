package com.detmir.ui.avatar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.detmir.recycli.annotations.RecyclerStateBinder
import com.detmir.recycli.annotations.RecyclerStateView

@RecyclerStateView
class AvatarItemSquaredCornersView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    @RecyclerStateBinder
    fun bindState(state: AvatarItem.State) {
        setBackgroundColor(Color.parseColor("#ffffff00"))
        text = "bind Avatar Squared Corners ${state.text}"
    }

}