package com.detmir.ui.stone

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView

@SuppressLint("AppCompatCustomView")
@RecyclerItemView
class StoneItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {


    @RecyclerItemStateBinder
    fun bindState(state: StoneItem.State) {
        text = "bind Stone ${state.text}"
    }

}