package com.detmir.shapes.circle

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.detmir.recycli.annotations.RecyclerStateView


@SuppressLint("AppCompatCustomView")
@RecyclerStateView
class CircleItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr), CircleItem.View {


    override fun bindState(state: CircleItem.State) {
        text = "CIRCLE ITEM ${state.text}"
    }

}