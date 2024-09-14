package com.example.paging3_tvshowsapi.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TVmazeService {

    @GET("/shows")

    suspend fun getTvShows(@Query("page") page:Int): Response<List<TvShowDto>>




}