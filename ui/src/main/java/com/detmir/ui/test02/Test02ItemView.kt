package com.detmir.ui.test02

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.detmir.recycli.annotations.RecyclerStateBinder
import com.detmir.recycli.annotations.RecyclerStateView

@SuppressLint("AppCompatCustomView")
@RecyclerStateView
class Test02ItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    @RecyclerStateBinder
    fun doIt(state: Test02Item.State) {
         when (state) {
             is Test02Item.State.One,
             is Test02Item.State.Two.Two_1,
             is Test02Item.State.Two.Two_2,
             is Test02Item.State.Two.Two_3,
             is Test02Item.State.Three -> {
                 text = state.text
             }
         }
    }


    @RecyclerStateBinder
    fun doIt2(state: Test02Item.State.Two) {
        when (state) {
            is Test02Item.State.Two.Two_1,
            is Test02Item.State.Two.Two_2,
            is Test02Item.State.Two.Two_3 -> {
                text = "Im sealed" + state.text
            }

        }
    }

}