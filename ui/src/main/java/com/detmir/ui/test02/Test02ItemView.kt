package com.detmir.ui.test02

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView

@SuppressLint("AppCompatCustomView")
@RecyclerItemView
class Test02ItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    @RecyclerItemStateBinder
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


    @RecyclerItemStateBinder
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