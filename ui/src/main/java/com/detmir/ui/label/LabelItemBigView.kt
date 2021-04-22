package com.detmir.ui.label

import android.content.Context
import android.util.AttributeSet
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView


@RecyclerItemView
class LabelItemBigView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    @RecyclerItemStateBinder
    fun bindState(state: LabelItem.State.Big) {
        textSize = 27f
        text = "bind Label Big ${state.text}"
    }

}