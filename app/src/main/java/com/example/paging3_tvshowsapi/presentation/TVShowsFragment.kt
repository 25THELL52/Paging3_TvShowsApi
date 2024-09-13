package com.example.paging3_tvshowsapi.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paging3_tvshowsapi.R
//import com.example.paging3_tvshowsapi.R

import com.example.paging3_tvshowsapi.databinding.FragmentTvShowsBinding
import com.example.paging3_tvshowsapi.presentation.paging.TvShowsAdapter



import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [TVShowsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class TVShowsFragment : Fragment() {

    private val tvShowsViewModel: TVShowsViewModel by viewModels()
    private lateinit var binding: FragmentTvShowsBinding

    @Inject
    lateinit var adapter: TvShowsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTvShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            tvShows.adapter = adapter
            tvShows.layoutManager = LinearLayoutManager(requireContext())



            viewLifecycleOwner.lifecycleScope.launch {
                adapter.loadStateFlow.collect {


                    if (it.refresh is LoadState.NotLoading) {
                        tvShows.scrollToPosition(0)
                        noresultsTv.isVisible = adapter.itemCount == 0
                        // https://www.reddit.com/r/androiddev/comments/lb7ys6/scrolling_up_after_refresh_with_pagingdataadapter/
                        Log.e("track error", "scroll to 0")


                    }
                    loadingPg.isVisible = it.refresh is LoadState.Loading
                    tvShows.isVisible = it.source.refresh is LoadState.NotLoading


                }
            }


        }




        viewLifecycleOwner.lifecycleScope.launch {
            tvShowsViewModel.tvShows.collectLatest {

                Log.e("track error", "data submitted to adapter")
                Log.e(
                    "track error",
                    "data item count after submission: ${adapter.snapshot().items.size} "
                )
                adapter.notifyDataSetChanged()
                adapter.submitData(it)


            }


        }


    }


}
