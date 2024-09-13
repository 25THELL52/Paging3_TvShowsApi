package com.example.paging3_tvshowsapi.domain

import androidx.paging.PagingData
import com.example.paging3_tvshowsapi.data.TvShow
import kotlinx.coroutines.flow.Flow

interface TvShowsRepository {
    fun getTvShows(): Flow<PagingData<TvShow>>
}