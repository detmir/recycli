package com.detmir.ui.label

import android.content.Context
import android.util.AttributeSet
import com.detmir.recycli.annotations.RecyclerStateBinder
import com.detmir.recycli.annotations.RecyclerStateView

@RecyclerStateView
class LabellItemSmalView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    @RecyclerStateBinder
    fun bindState(state: LabelItem.State.Small) {
        textSize = 12f
        text = "bind Label Small ${state.text}"
    }

}