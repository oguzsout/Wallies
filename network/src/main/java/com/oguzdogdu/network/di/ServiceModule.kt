package com.oguzdogdu.network.di

import android.service.wallpaper.WallpaperService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideWallpaperService(retrofit: Retrofit): com.oguzdogdu.network.service.WallpaperService {
        return retrofit.create(com.oguzdogdu.network.service.WallpaperService::class.java)
    }
}
