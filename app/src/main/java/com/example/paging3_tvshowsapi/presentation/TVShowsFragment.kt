package com.example.paging3_tvshowsapi.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager


import com.example.paging3_tvshowsapi.databinding.FragmentTvShowsBinding
import com.example.paging3_tvshowsapi.presentation.paging.TvShowsAdapter
import com.example.paging3_tvshowsapi.presentation.paging.TvShowsLoadStateFooterAdapter
import com.example.paging3_tvshowsapi.util.networkTracker.NetworkConnectivityService
import com.example.paging3_tvshowsapi.util.networkTracker.NetworkConnectivityServiceImpl
import com.example.paging3_tvshowsapi.util.networkTracker.NetworkStatus


import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
    private lateinit var previousNetworkStatus: NetworkStatus


    @Inject
    lateinit var adapter: TvShowsAdapter

    @Inject
    lateinit var networkConnectivityService: NetworkConnectivityService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTvShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {


            val footer = TvShowsLoadStateFooterAdapter() { adapter.retry() }
            tvShows.adapter = adapter.withLoadStateFooter(

                footer = footer
            )

            adapter.addLoadStateListener { loadStates ->

                Log.e("track isadapterautorefreshed", "inside adapter load state listener")

                if (adapter.itemCount == 0) {

                    //header.loadState = loadStates.refresh
                    loadingPg.isVisible = loadStates.refresh is LoadState.Loading
                    retryButton.isVisible = loadStates.refresh is LoadState.Error
                    tvShowsSwipeToRefreshLt.isVisible = false

                }

                if (adapter.itemCount != 0) {

                    tvShowsSwipeToRefreshLt.isVisible = true
                    Log.e("track internet", "${networkConnectivityService.checkForInternet()}")


                    if(loadStates.refresh is LoadState.Error) tvShowsSwipeToRefreshLt.isRefreshing = false

                    /*if (!checkForInternet()) {


                        tvShowsSwipeToRefreshLt.isRefreshing =
                            loadStates.refresh is LoadState.Loading
                    }

                     */


                }


            }

            tvShows.layoutManager = LinearLayoutManager(requireContext())

            viewLifecycleOwner.lifecycleScope.launch {
                adapter.loadStateFlow.collect {


                    //tvShows.scrollToPosition(0)


                    //(it.refresh is LoadState.Error || it.refresh is LoadState.NotLoading)

                    // https://www.reddit.com/r/androiddev/comments/lb7ys6/scrolling_up_after_refresh_with_pagingdataadapter/
                    //Log.e("track error", "scroll to 0")


                    //tvShows.isVisible = true

                }
            }

            tvShowsSwipeToRefreshLt.setOnRefreshListener {
                if (networkConnectivityService.checkForInternet()) {


                    adapter.refresh()
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(5000)
                        withContext(Dispatchers.Main){
                            tvShowsSwipeToRefreshLt.isRefreshing = false

                        }
                    }


                    }


                else {

                    tvShowsSwipeToRefreshLt.isRefreshing = false

                }


            }

            retryButton.setOnClickListener {
                adapter.refresh()
            }


        }

        viewLifecycleOwner.lifecycleScope.launch {
            tvShowsViewModel.tvShows.collectLatest {
                repeatOnLifecycle(Lifecycle.State.STARTED) {


                    Log.e("track error", "data submitted to adapter")
                    Log.e(
                        "track error", "data item count after submission: ${binding.tvShows.size} "
                    )
                    //adapter.notifyDataSetChanged()

                    adapter.submitData(it)

                    Log.e(
                        "track error", "data item count after submission: ${binding.tvShows.size} "
                    )


                }
            }


        }




        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                tvShowsViewModel._previousNetworkStatus.collectLatest {
                    previousNetworkStatus = it
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                tvShowsViewModel.networkStatus.collectLatest {

                    when (it) {

                        NetworkStatus.Connected -> {
                            Log.e(
                                "track internet 2 ",
                                "checkForInternet() from Connected: ${networkConnectivityService.checkForInternet()}"
                            )

                            if (networkConnectivityService.checkForInternet()) {
                                if (previousNetworkStatus is NetworkStatus.Disconnected) {


                                    tvShowsViewModel.setPreviousNetworkStatus(NetworkStatus.Connected)
                                    showOnlineStatusTvWhenOnline()
                                    Log.e("track internet 2 ", "Connected")

                                } else tvShowsViewModel.setPreviousNetworkStatus(NetworkStatus.Connected)


                            }
                        }

                        NetworkStatus.Disconnected -> {


                            if (previousNetworkStatus is NetworkStatus.Unknown) {

                                showOnlineStatusTvWhenOffline()
                                tvShowsViewModel.setPreviousNetworkStatus(NetworkStatus.Disconnected)
                                Log.e("track internet 2 ", "Disconnected from launch condition")

                            } else {
                                Log.e(
                                    "track internet 2 ",
                                    "checkForInternet() from Disconnected: ${networkConnectivityService.checkForInternet()}"
                                )

                                if (!networkConnectivityService.checkForInternet()) {
                                    delay(4000)

                                    showOnlineStatusTvWhenOffline()



                                    tvShowsViewModel.setPreviousNetworkStatus(NetworkStatus.Disconnected)
                                    Log.e("track internet 2 ", "Disconnected")
                                }
                            }


                        }

                        NetworkStatus.Unknown -> Log.e("track internet 2 ", "Unknown")
                    }
                }
            }
        }
    }


    private fun showOnlineStatusTvWhenOffline() {

        binding.apply {
            onlineStatusTv.text = "You're offline"
            onlineStatusTv.setBackgroundColor(Color.DKGRAY)
            onlineStatusTv.setTextColor(Color.WHITE)
            onlineStatusTv.isGone = false

        }

    }

    private suspend fun showOnlineStatusTvWhenOnline() {

        binding.apply {

            onlineStatusTv.isGone = false
            onlineStatusTv.text = "Back Online"
            onlineStatusTv.setBackgroundColor(Color.CYAN)
            onlineStatusTv.setTextColor(Color.BLACK)
            delay(2430)
            onlineStatusTv.isGone = true
        }

    }



}
