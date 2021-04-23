package com.detmir.kkppt3.viewholders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.detmir.kkppt3.R
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemViewHolder
import com.detmir.recycli.annotations.RecyclerItemViewHolderCreator

@RecyclerItemViewHolder
class ServerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val serverAddress: TextView = view.findViewById(R.id.server_item_title)

    @RecyclerItemStateBinder
    fun bindState(serverItem: ServerItem) {
        serverAddress.text = serverItem.serverAddress
    }

    companion object {
        @RecyclerItemViewHolderCreator
        fun provideViewHolder(context: Context): ServerItemViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.server_item_view, null)
                .apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            return ServerItemViewHolder(view)
        }
    }
}
