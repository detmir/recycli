package com.detmir.ui.bottom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView
import com.detmir.ui.R

@RecyclerItemView
class BottomLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var progress: ProgressBar
    var buttonNext: Button
    var buttonError: Button
    lateinit var state: BottomLoading.State

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        inflater.inflate(R.layout.bottom_loading_view, this, true)
        progress = findViewById(R.id.bottom_loading_view_progress)
        buttonNext = findViewById(R.id.bottom_loading_view_button_next)
        buttonError = findViewById(R.id.bottom_loading_view_button_error)

        buttonNext.setOnClickListener {
            handleClick()
        }

        buttonError.setOnClickListener {
            handleClick()
        }
    }

    private fun handleClick() {
        when (val s = state) {
            is BottomLoading.State.Button -> s.next.invoke()
            is BottomLoading.State.Error -> s.reload.invoke()
        }
    }

    @RecyclerItemStateBinder
    fun bindState(state: BottomLoading.State) {
        this.state = state
        tag = state.provideId()
        when (state) {
            is BottomLoading.State.Progress -> {
                buttonNext.visibility = View.GONE
                buttonError.visibility = View.GONE
                progress.visibility = View.VISIBLE
            }
            is BottomLoading.State.Button -> {
                buttonNext.visibility = View.VISIBLE
                buttonError.visibility = View.GONE
                progress.visibility = View.GONE

            }
            is BottomLoading.State.Dummy -> {
                buttonNext.visibility = View.GONE
                buttonError.visibility = View.GONE
                progress.visibility = View.GONE
            }

            is BottomLoading.State.Error -> {
                buttonNext.visibility = View.GONE
                buttonError.visibility = View.VISIBLE
                progress.visibility = View.GONE
            }
        }
    }
}