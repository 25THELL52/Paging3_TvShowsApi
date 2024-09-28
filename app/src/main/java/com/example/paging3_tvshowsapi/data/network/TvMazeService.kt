package com.example.paging3_tvshowsapi.data.network

import com.example.paging3_tvshowsapi.data.network.model.TvShowDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TvMazeService {

    @GET("/shows")

    suspend fun getTvShows(@Query("page") page:Int): Response<List<TvShowDto>>


}