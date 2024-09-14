package com.example.paging3_tvshowsapi.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.paging3_tvshowsapi.data.network.Image
import com.example.paging3_tvshowsapi.data.network.Rating

@Entity(tableName = "tv_shows")
data class TvShow(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(name = "Name")
    val name: String?,

    @ColumnInfo(name= "Language")
    val language : String?,

    @ColumnInfo("Rating")
    val rating:Rating?,

    @ColumnInfo(name="Image")
    val image: Image?

)
