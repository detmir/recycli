package com.detmir.kkppt3.viewholders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.detmir.kkppt3.R
import com.detmir.kkppt3.views.CloudItem
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemViewHolder
import com.detmir.recycli.annotations.RecyclerItemViewHolderCreator

@RecyclerItemViewHolder
class DigitalOceanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.cloud_digital_ocean_item_name)

    @RecyclerItemStateBinder
    fun bindState(cloudItem: CloudItem) {
        name.text = cloudItem.serverName
    }

    companion object {
        @RecyclerItemViewHolderCreator
        fun provideViewHolder(context: Context): DigitalOceanViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cloud_digital_ocean_item_view, null)
                .apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            return DigitalOceanViewHolder(view)
        }
    }
}
