package com.detmir.ui.test01

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.detmir.recycli.annotations.RecyclerStateBinder
import com.detmir.recycli.annotations.RecyclerStateView

@SuppressLint("AppCompatCustomView")
@RecyclerStateView
class Test01ItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr), Test01Item.View {


    @RecyclerStateBinder
    override fun bindState(state: Test01Item.State) {
        text = state.text
    }

}