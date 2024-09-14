package com.example.paging3_tvshowsapi.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TvShowsDAO {

@Query("SELECT * FROM tv_shows")
fun getTvShows(): PagingSource<Int, TvShow>


@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertAll(tvShows: List<TvShow>)

@Query("DELETE  FROM tv_shows")
suspend fun deleteAll()
}