package com.example.paging3_tvshowsapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [TvShow::class], version = 1)
@TypeConverters(StringImageConverter::class,StringRatingConverter::class)

abstract class TvShowsDatabase: RoomDatabase(){
    abstract fun getTvShowsDAO(): TvShowsDAO


}