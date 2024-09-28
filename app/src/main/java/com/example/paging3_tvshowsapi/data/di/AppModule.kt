package com.example.paging3_tvshowsapi.data.di

import android.content.Context
import com.example.paging3_tvshowsapi.data.TVmazeService
import com.example.paging3_tvshowsapi.data.TvShowsRepositoryImpl
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

    fun providesTvShowRepository(tvmazeService: TVmazeService): TvShowsRepository {
        return TvShowsRepositoryImpl(tvmazeService)
    }

    @Singleton
    @Provides
    fun providesTVmazeService(retrofit: Retrofit): TVmazeService {


        return retrofit.create(TVmazeService::class.java)

    }

    @Singleton
    @Provides
    fun providesRetrofit(BaseUrl: String, client: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BaseUrl)
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
    fun providesNetworkConnectivityService(@ApplicationContext context: Context): NetworkConnectivityService =
        NetworkConnectivityServiceImpl(context)
}