package com.example.paging3_tvshowsapi.presentation.paging

import android.net.http.HttpException
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.paging3_tvshowsapi.data.local.TvShow
import com.example.paging3_tvshowsapi.data.local.TvShowsDatabase
import com.example.paging3_tvshowsapi.data.network.TVmazeService
import com.example.paging3_tvshowsapi.data.toTvShow
import java.io.IOException


private const val STARTING_PAGE_INDEX = 1
private const val NETWORK_PAGE_SIZE = 250

@OptIn(ExperimentalPagingApi::class)
class TvShowsRemoteMediator(
    private val tvMazeService: TVmazeService,
    private val tvShowsDatabase: TvShowsDatabase
) : RemoteMediator<Int, TvShow>() {


    override suspend fun load(loadType: LoadType, state: PagingState<Int, TvShow>): MediatorResult {

        return try {
            // The network load method takes an optional after=<user.id>
            // parameter. For every page after the first, pass the last user
            // ID to let it continue from where it left off. For REFRESH,
            // pass null to load the first page.
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )


                    // You must explicitly check if the last item is null when
                    // appending, since passing null to networkService is only
                    // valid for initial load. If lastItem is null it means no
                    // items were loaded after the initial REFRESH and there are
                    // no more items to load.

                    (lastItem.id)/ STARTING_PAGE_INDEX + 1
                }
            }

            // Suspending network load via Retrofit. This doesn't need to be
            // wrapped in a withContext(Dispatcher.IO) { ... } block since
            // Retrofit's Coroutine CallAdapter dispatches on a worker
            // thread.
            val response = tvMazeService.getTvShows(
                page
            )

            val tvShowDao = tvShowsDatabase.getTvShowsDAO()
            tvShowsDatabase.withTransaction{
                if (loadType == LoadType.REFRESH) {
                    tvShowDao.deleteAll()
                }

                // Insert new users into database, which invalidates the
                // current PagingData, allowing Paging to present the updates
                // in the DB.
                response.body()?.let {tvShowDao.insertAll(it.map { tvShowDto ->  tvShowDto.toTvShow() })  }
            }

            MediatorResult.Success(
                endOfPaginationReached = response.body().isNullOrEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: retrofit2.HttpException) {
            MediatorResult.Error(e)
        }


    }
}