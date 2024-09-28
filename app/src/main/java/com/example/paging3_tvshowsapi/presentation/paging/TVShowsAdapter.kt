package com.example.paging3_tvshowsapi.presentation.paging

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.paging3_tvshowsapi.data.local.TvShow
import com.example.paging3_tvshowsapi.databinding.TvshowsItemBinding
import javax.inject.Inject

class TvShowsAdapter()  :
    PagingDataAdapter<TvShow, TvShowsAdapter.TvShowsViewHolder>(differCallback) {

    private lateinit var binding: TvshowsItemBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowsViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        binding = TvshowsItemBinding.inflate(inflater, parent, false)
        return TvShowsViewHolder()
    }

    override fun onBindViewHolder(holder: TvShowsViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.setIsRecyclable(false)
    }


    inner class TvShowsViewHolder() : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SuspiciousIndentation")
        fun bind(item: TvShow) {
            binding.apply {
                showNameTv.text = item.name
                languageTv.text = item.language
                ratingBr.rating = ((item.rating?.average?.toFloat())?.div(2)) ?: 0.0f

                //coroutineScope.launch() {  }


                Glide.with(context)


                    .load(item.image?.medium)
                    .override(300, 200)
                    .into(imageView)

            }
        }
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<TvShow>() {
            override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
                return oldItem == newItem
            }
        }
    }
}



