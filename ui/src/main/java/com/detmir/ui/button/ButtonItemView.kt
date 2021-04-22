package com.detmir.ui.button

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView

@RecyclerItemView
class ButtonItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    @RecyclerItemStateBinder
    fun bindState(state: ButtonItem.State.Error) {
        text = "bind Button Error ${state.text}"
    }

    @RecyclerItemStateBinder
    fun bindState(state: ButtonItem.State.Colored.Orange) {
        setBackgroundColor(Color.parseColor("#ffFFD700"))
        text = "bind Button Orange ${state.text}"
    }

    @RecyclerItemStateBinder
    fun bindState(state: ButtonItem.State.Colored.Green) {
        setBackgroundColor(Color.parseColor("#ff00ff00"))
        text = "bind Button Green ${state.text}"
    }

    @RecyclerItemStateBinder
    fun bindState(state: ButtonItem.State.Loading) {
        text = "bind Button Loading ${state.text}"
    }


}