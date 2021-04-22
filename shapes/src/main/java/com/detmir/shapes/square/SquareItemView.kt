package com.detmir.shapes.square

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView

@SuppressLint("AppCompatCustomView")
@RecyclerItemView
class SquareItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr), SquareItem.View {

    @RecyclerItemStateBinder
    override fun bindState(state: SquareItem.State) {
        text = "SQUARE ITEM ${state.text}"
    }

}