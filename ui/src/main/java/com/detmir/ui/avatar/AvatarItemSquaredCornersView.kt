package com.detmir.ui.avatar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView

@RecyclerItemView
class AvatarItemSquaredCornersView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    @RecyclerItemStateBinder
    fun bindState(state: AvatarItem.State) {
        setBackgroundColor(Color.parseColor("#ffffff00"))
        text = "bind Avatar Squared Corners ${state.text}"
    }

}