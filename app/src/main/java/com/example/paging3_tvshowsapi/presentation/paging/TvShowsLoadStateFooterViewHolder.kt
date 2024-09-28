package com.example.paging3_tvshowsapi.presentation.paging

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3_tvshowsapi.R
import com.example.paging3_tvshowsapi.databinding.TvShowsLoadStateFooterViewItemBinding

class TvShowsLoadStateFooterViewHolder(
    private val footerBinding: TvShowsLoadStateFooterViewItemBinding,
    retry: () -> Unit,

    ) : RecyclerView.ViewHolder(footerBinding.root) {

    init {
        footerBinding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        /*
        if (loadState is LoadState.Error) {
            footerBinding.errorMsg.text = loadState.error.localizedMessage
        }

         */


        footerBinding.progressBar.isGone = (loadState is LoadState.Error) || (loadState is LoadState.NotLoading)
        Log.e(
            "understandRemoteMediator: ",
            "loadstate is ERROR ? : ${loadState is LoadState.Error}"
        )

        footerBinding.retryButton.isGone = loadState is LoadState.Loading
        //footerBinding.errorMsg.isVisible = (loadState is LoadState.Error)
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): TvShowsLoadStateFooterViewHolder {
            val footerView = LayoutInflater.from(parent.context)
                .inflate(R.layout.tv_shows_load_state_footer_view_item, parent, false)


            val footerBinding = TvShowsLoadStateFooterViewItemBinding.bind(footerView)

            return TvShowsLoadStateFooterViewHolder(footerBinding, retry)
        }
    }
}