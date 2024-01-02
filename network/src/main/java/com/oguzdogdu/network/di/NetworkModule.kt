package com.oguzdogdu.network.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.oguzdogdu.network.BuildConfig
import com.oguzdogdu.network.common.Constants.UNSPLASH_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    @InterceptorLogging
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.BUILD_TYPE != "release") {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    @WalliesOkHttpClient
    fun provideOkHttpClient(
        @InterceptorLogging loggingInterceptor: HttpLoggingInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        val cacheSize = (10 * 1024 * 1024).toLong()
        val cache = Cache(context.cacheDir, cacheSize)
        val builder = OkHttpClient.Builder().apply {
            cache(cache)
            addInterceptor(loggingInterceptor)
            addInterceptor(ChuckerInterceptor(context))
            addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val newRequest = originalRequest.newBuilder()
                        .addHeader("Authorization", "Client-ID ${BuildConfig.API_KEY}")
                        .build()
                    chain.proceed(newRequest)
                }
            addInterceptor {
                val response: Response = it.proceed(it.request())
                val cacheControl = CacheControl.Builder()
                    .maxStale(2, TimeUnit.HOURS)
                    .build()
                 response.newBuilder()
                    .header("Cache-Control", cacheControl.toString())
                    .build()
            }
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            followRedirects(true)
            retryOnConnectionFailure(true)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    @WalliesRetrofit
    fun provideRetrofit(
        @WalliesOkHttpClient okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(UNSPLASH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
