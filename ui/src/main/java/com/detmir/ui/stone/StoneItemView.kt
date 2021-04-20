package com.detmir.ui.stone

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.detmir.recycli.annotations.RecyclerStateBinder
import com.detmir.recycli.annotations.RecyclerStateView

@SuppressLint("AppCompatCustomView")
@RecyclerStateView
class StoneItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {


    @RecyclerStateBinder
    fun bindState(state: StoneItem.State) {
        text = "bind Stone ${state.text}"
    }

}