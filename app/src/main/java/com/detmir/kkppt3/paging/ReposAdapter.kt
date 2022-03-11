package com.detmir.kkppt3.paging

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter

class ReposAdapter: PagingDataAdapter<Repo, RepoViewHolder>(RepoDiffCallback()) {
    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = getItem(position)
        if (repo != null) {
            (holder.itemView as RepoItemView).bindState(repo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val repoView = RepoItemView(parent.context)
        return RepoViewHolder(repoView)
    }


}
