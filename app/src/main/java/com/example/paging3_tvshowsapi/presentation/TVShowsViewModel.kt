package com.example.paging3_tvshowsapi.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import androidx.paging.PagingData
import androidx.paging.cachedIn

import com.example.paging3_tvshowsapi.data.TvShow
import com.example.paging3_tvshowsapi.domain.TvShowsRepository
import com.example.paging3_tvshowsapi.util.networkTracker.NetworkConnectivityService
import com.example.paging3_tvshowsapi.util.networkTracker.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

import javax.inject.Inject

@HiltViewModel
class TVShowsViewModel @Inject constructor(private val tvShowsRepository: TvShowsRepository,
                                           private val networkConnectivityService: NetworkConnectivityService
) :
    ViewModel() {

    private val tvShowsStateFlow = MutableStateFlow<Boolean>(false)

    val tvShows: Flow<PagingData<TvShow>> = tvShowsRepository.getTvShows().cachedIn(viewModelScope)

    private var previousNetworkStatus: MutableStateFlow<NetworkStatus> =
        MutableStateFlow(NetworkStatus.Unknown)
    val _previousNetworkStatus = previousNetworkStatus


    val networkStatus: StateFlow<NetworkStatus> = networkConnectivityService.networkStatus.stateIn(
        initialValue = NetworkStatus.Unknown,
        scope = viewModelScope,
        started = WhileSubscribed(5000)
    )




    fun setPreviousNetworkStatus(networkStatus: NetworkStatus) {
        previousNetworkStatus.value = networkStatus
    }



}

