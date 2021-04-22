package com.detmir.ui.test02

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView

@SuppressLint("AppCompatCustomView")
@RecyclerItemView
class Test02TriItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    @RecyclerItemStateBinder
    fun allo(state: Test02Item.State.Two.Two_1) {
        text = "I m special " + state.text
    }
}