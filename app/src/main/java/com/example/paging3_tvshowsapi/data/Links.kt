package com.example.paging3_tvshowsapi.data

import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("previousepisode")
    val previousEpisode: Previousepisode,
    val self: Self
)