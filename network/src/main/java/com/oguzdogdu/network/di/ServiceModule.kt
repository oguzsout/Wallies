package com.oguzdogdu.network.di

import com.oguzdogdu.network.service.UnsplashUserService
import com.oguzdogdu.network.service.WallpaperService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideWallpaperService(@WalliesRetrofit retrofit: Retrofit): WallpaperService =
        retrofit.create(WallpaperService::class.java)


    @Provides
    @Singleton
    fun provideUnsplashUserService(@WalliesRetrofit retrofit: Retrofit): UnsplashUserService =
        retrofit.create(UnsplashUserService::class.java)
}
