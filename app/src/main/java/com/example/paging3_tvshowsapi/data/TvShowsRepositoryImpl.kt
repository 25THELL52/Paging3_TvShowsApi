package com.example.paging3_tvshowsapi.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.paging3_tvshowsapi.domain.TvShowsRepository
import com.example.paging3_tvshowsapi.presentation.paging.TVShowsPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvShowsRepositoryImpl @Inject constructor(private val tvMazeService: TVmazeService) : TvShowsRepository {
    override fun getTvShows(): Flow<PagingData<TvShow>> {
        return Pager(config = PagingConfig(pageSize = 250, initialLoadSize = 250*3),
            pagingSourceFactory = { TVShowsPagingSource(tvMazeService) }).flow

    }


}