package com.detmir.ui.test01

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView

@SuppressLint("AppCompatCustomView")
@RecyclerItemView
class Test01RedItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr), Test01Item.View {

    init {
        setBackgroundColor(Color.parseColor("#ffff0000"))
    }

    @RecyclerItemStateBinder
    override fun bindState(state: Test01Item.State) {
        text = state.text
    }

}