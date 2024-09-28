package com.example.paging3_tvshowsapi.data.local

import androidx.room.TypeConverter
import com.example.paging3_tvshowsapi.data.network.model.Rating

object StringRatingConverter {

    @TypeConverter
    @JvmStatic
    fun ratingToString(rating: Rating?): String? =

        rating?.average?.toString()



    @TypeConverter
    @JvmStatic
    fun stringToRating(string: String?): Rating? =
        if(string.isNullOrBlank())  null
        else {

            Rating(string.toDouble())
        }
}