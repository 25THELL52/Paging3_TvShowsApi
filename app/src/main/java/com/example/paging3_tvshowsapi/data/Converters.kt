package com.example.paging3_tvshowsapi.data

import com.example.paging3_tvshowsapi.data.local.TvShow
import com.example.paging3_tvshowsapi.data.network.TvShowDto


fun TvShowDto.toTvShow(): TvShow {

    return TvShow(id,name,language,rating,image)
}