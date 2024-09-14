package com.example.paging3_tvshowsapi.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.paging3_tvshowsapi.data.local.TvShow
import com.example.paging3_tvshowsapi.data.local.TvShowsDatabase
import com.example.paging3_tvshowsapi.domain.TvShowsRepository
import com.example.paging3_tvshowsapi.presentation.paging.TvShowsRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvShowsRepositoryImpl @Inject constructor( private val tvmazeService: TVmazeService,
                                                 private val tvShowsDatabase: TvShowsDatabase) : TvShowsRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getTvShows(): Flow<PagingData<TvShow>> {

        return Pager(
            config = PagingConfig(pageSize = 250, initialLoadSize = 250*3),
            remoteMediator = TvShowsRemoteMediator(tvmazeService,tvShowsDatabase))
        { tvShowsDatabase.getTvShowsDAO().getTvShows() }.flow


    }


}