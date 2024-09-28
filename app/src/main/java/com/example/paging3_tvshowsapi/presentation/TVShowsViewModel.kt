package com.example.paging3_tvshowsapi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.paging3_tvshowsapi.data.local.TvShow

import com.example.paging3_tvshowsapi.domain.TvShowsRepository
import com.example.paging3_tvshowsapi.util.networkTracker.NetworkConnectivityService
import com.example.paging3_tvshowsapi.util.networkTracker.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class TVShowsViewModel @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
    private val networkConnectivityService: NetworkConnectivityService
) :
    ViewModel() {


    val tvShows: Flow<PagingData<TvShow>> = tvShowsRepository.getTvShows()
    var tvShowsItemCount = tvShowsRepository.getDatabaseTvShowsTableItemCount()

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

