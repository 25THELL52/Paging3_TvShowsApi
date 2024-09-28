package com.example.paging3_tvshowsapi.domain

import androidx.paging.PagingData
import com.example.paging3_tvshowsapi.data.local.TvShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TvShowsRepository {
    fun getTvShows(): Flow<PagingData<TvShow>>
    fun getDatabaseTvShowsTableItemCount(): Flow<Int>
}