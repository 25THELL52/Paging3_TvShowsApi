package com.example.paging3_tvshowsapi.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import androidx.paging.PagingData
import androidx.paging.cachedIn

import com.example.paging3_tvshowsapi.data.TvShow
import com.example.paging3_tvshowsapi.domain.TvShowsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

import javax.inject.Inject

@HiltViewModel
class TVShowsViewModel @Inject constructor(private val tvShowsRepository: TvShowsRepository) :
    ViewModel() {

    private val tvShowsStateFlow = MutableStateFlow<Boolean>(false)

    val tvShows: Flow<PagingData<TvShow>> = tvShowsRepository.getTvShows().cachedIn(viewModelScope)





}

