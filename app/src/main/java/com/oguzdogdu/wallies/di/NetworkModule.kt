package com.oguzdogdu.wallies.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.oguzdogdu.data.common.Constants.UNSPLASH_BASE_URL
import com.oguzdogdu.data.source.remote.WallpaperService
import com.oguzdogdu.wallies.BuildConfig.API_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        val builder = OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(ChuckerInterceptor(context))
            addInterceptor {
                val originalRequest = it.request()
                val newHttpUrl = originalRequest.url.newBuilder()
                    .addQueryParameter("client_id", API_KEY)
                    .build()
                val newRequest = originalRequest.newBuilder()
                    .url(newHttpUrl)
                    .build()
                it.proceed(newRequest)
            }
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            followSslRedirects(true)
            followRedirects(true)
            retryOnConnectionFailure(true)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(UNSPLASH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCharacterService(retrofit: Retrofit): WallpaperService {
        return retrofit.create(WallpaperService::class.java)
    }
}
