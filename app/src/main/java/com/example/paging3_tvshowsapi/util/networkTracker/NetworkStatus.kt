package com.example.paging3_tvshowsapi.util.networkTracker

sealed class NetworkStatus {
    data object Unknown: NetworkStatus()
    data object Connected: NetworkStatus()
    data object Disconnected: NetworkStatus()
}