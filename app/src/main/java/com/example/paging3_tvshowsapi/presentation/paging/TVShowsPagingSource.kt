package com.example.paging3_tvshowsapi.presentation.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState

import com.example.paging3_tvshowsapi.data.TVmazeService
import com.example.paging3_tvshowsapi.data.TvShow


private const val STARTING_PAGE_INDEX = 0
private const val NETWORK_PAGE_SIZE = 250


class TVShowsPagingSource(private val tvMazeService: TVmazeService ) :
    PagingSource<Int, TvShow>() {
    override fun getRefreshKey(state: PagingState<Int, TvShow>): Int? {
        return  state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShow> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = tvMazeService.getTvShows(page)


            //Log.e("track error","${response.body()}")
            val newData : List<TvShow> = response.body()!!




            Log.e("track error","newData filtered")
            Log.e("track error","newData size :${newData.size} ")

            Log.e("track error","newData first element ${newData[0].name}")


            LoadResult.Page(
                data =  newData ,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page.minus(1),
                nextKey = if (response.body()!!.isEmpty()) null else page.plus(params.loadSize/NETWORK_PAGE_SIZE)
            )



        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }




}