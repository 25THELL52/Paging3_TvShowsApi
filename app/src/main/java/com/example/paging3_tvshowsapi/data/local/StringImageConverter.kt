package com.example.paging3_tvshowsapi.data.local

import android.util.Log
import androidx.room.TypeConverter
import com.example.paging3_tvshowsapi.data.network.Image

object StringImageConverter {

    @TypeConverter
    @JvmStatic
    fun imageToString(image: Image?): String {
        return if (image == null) ""
        else {

            "${image.medium}|${image.original}"
        }

    }

    @TypeConverter
    @JvmStatic
    fun stringToImage(string: String?): Image? =
        if(string.isNullOrBlank())  null
        else {

            val list = string.split("|").toList()
            Image(list[0],list[1])

        }
}
