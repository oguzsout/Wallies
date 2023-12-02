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
import okhttp3.OkHttpClient
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
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val newRequest = originalRequest.newBuilder()
                        .addHeader("Authorization", "Client-ID ${BuildConfig.API_KEY}")
                        .build()
                    chain.proceed(newRequest)
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
}
