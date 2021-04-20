package com.detmir.ui.radio

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.detmir.recycli.annotations.RecyclerStateBinder
import com.detmir.recycli.annotations.RecyclerStateView

@SuppressLint("AppCompatCustomView")
@RecyclerStateView
class RadioItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr), RadioItem.View {


    @RecyclerStateBinder
    override fun bindState(state: RadioItem.State) {
        text = "bind Radio ${state.text}"
    }

}