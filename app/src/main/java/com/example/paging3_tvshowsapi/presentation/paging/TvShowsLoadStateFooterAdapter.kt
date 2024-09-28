package com.example.paging3_tvshowsapi.presentation.paging

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class TvShowsLoadStateFooterAdapter( private val retry: () -> Unit

                            ) : LoadStateAdapter<TvShowsLoadStateFooterViewHolder>() {
    override fun onBindViewHolder(holder: TvShowsLoadStateFooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): TvShowsLoadStateFooterViewHolder {
        return TvShowsLoadStateFooterViewHolder.create(parent, retry)
    }
}