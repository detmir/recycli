package com.detmir.shapes.line

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.detmir.recycli.annotations.RecyclerStateBinder
import com.detmir.recycli.annotations.RecyclerStateView


@SuppressLint("AppCompatCustomView")
@RecyclerStateView
class LineItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr), LineItem.View {


    @RecyclerStateBinder
    override fun bindState(state: LineItem.State) {
        when (state) {
            is LineItem.State.Arc -> text = "LINE ITEM ARC ${state.text}"
            is LineItem.State.Bezier -> text = "LINE ITEM BEZIER ${state.text}"
        }

    }

}