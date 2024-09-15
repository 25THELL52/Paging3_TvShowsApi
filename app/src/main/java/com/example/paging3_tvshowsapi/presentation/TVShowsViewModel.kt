package com.example.paging3_tvshowsapi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.paging3_tvshowsapi.data.local.TvShow

import com.example.paging3_tvshowsapi.domain.TvShowsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

import javax.inject.Inject

@HiltViewModel
class TVShowsViewModel @Inject constructor(private val tvShowsRepository: TvShowsRepository) :
    ViewModel() {



    val tvShows: Flow<PagingData<TvShow>> = tvShowsRepository.getTvShows()





}

