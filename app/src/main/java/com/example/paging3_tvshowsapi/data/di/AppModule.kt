package com.example.paging3_tvshowsapi.data.di

import android.content.Context
import androidx.room.Room
import com.example.paging3_tvshowsapi.data.local.TvShowsDatabase
 import com.example.paging3_tvshowsapi.data.TvShowsRepositoryImpl
import com.example.paging3_tvshowsapi.data.network.TvMazeService
import com.example.paging3_tvshowsapi.domain.SharedPreferencesHelper
import com.example.paging3_tvshowsapi.domain.TvShowsRepository
import com.example.paging3_tvshowsapi.presentation.paging.TvShowsAdapter
import com.example.paging3_tvshowsapi.util.networkTracker.NetworkConnectivityService
import com.example.paging3_tvshowsapi.util.networkTracker.NetworkConnectivityServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesBaseUrl(): String = "https://api.tvmaze.com"

    @Singleton
    @Provides
    fun providesTvShowAdapter(): TvShowsAdapter = TvShowsAdapter()

    @Singleton
    @Provides

    fun providesTvShowRepository(
        tvMazeService: TvMazeService,
        tvShowsDatabase: TvShowsDatabase
    ): TvShowsRepository {
        return TvShowsRepositoryImpl(tvMazeService, tvShowsDatabase)
    }

    @Singleton
    @Provides
    fun providesTvMazeService(retrofit: Retrofit): TvMazeService {


        return retrofit.create(TvMazeService::class.java)

    }

    @Singleton
    @Provides
    fun providesRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    }


    @Singleton
    @Provides
    fun providesOkhttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = Level.BASIC
        return httpLoggingInterceptor

    }

    @Singleton
    @Provides
    fun providesTvShowsDatabase(@ApplicationContext context: Context): TvShowsDatabase {

        return Room.databaseBuilder(
            context,
            TvShowsDatabase::class.java, "tv_show_database"
        ).build()

    }

    @Singleton
    @Provides
    fun providesSharedPreferencesHelper(@ApplicationContext context: Context): SharedPreferencesHelper {

        return SharedPreferencesHelper(context)

    }


    @Singleton
    @Provides
    fun providesNetworkConnectivityService(@ApplicationContext context: Context): NetworkConnectivityService =
        NetworkConnectivityServiceImpl(context)


}