package com.oguzdogdu.network.di

import com.oguzdogdu.network.service.UnsplashUserService
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
    fun provideWallpaperService(retrofit: Retrofit): com.oguzdogdu.network.service.WallpaperService {
        return retrofit.create(com.oguzdogdu.network.service.WallpaperService::class.java)
    }

    @Provides
    @Singleton
    fun provideUnsplashUserService(retrofit: Retrofit): UnsplashUserService {
        return retrofit.create(UnsplashUserService::class.java)
    }
}
