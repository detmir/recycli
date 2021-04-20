package com.detmir.shapes.square

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.detmir.recycli.annotations.RecyclerStateBinder
import com.detmir.recycli.annotations.RecyclerStateView

@SuppressLint("AppCompatCustomView")
@RecyclerStateView
class SquareItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr), SquareItem.View {

    @RecyclerStateBinder
    override fun bindState(state: SquareItem.State) {
        text = "SQUARE ITEM ${state.text}"
    }

}