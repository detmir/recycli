package com.detmir.shapes.line

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView


@SuppressLint("AppCompatCustomView")
@RecyclerItemView
class LineItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr), LineItem.View {


    @RecyclerItemStateBinder
    override fun bindState(state: LineItem.State) {
        when (state) {
            is LineItem.State.Arc -> text = "LINE ITEM ARC ${state.text}"
            is LineItem.State.Bezier -> text = "LINE ITEM BEZIER ${state.text}"
        }

    }

}