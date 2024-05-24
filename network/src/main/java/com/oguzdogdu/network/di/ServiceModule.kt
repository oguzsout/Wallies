package com.oguzdogdu.network.di

import com.oguzdogdu.network.service.UnsplashBaseApiService
import com.oguzdogdu.network.service.UnsplashUserService
import com.oguzdogdu.network.service.WallpaperService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideWallpaperService(httpClient: HttpClient): WallpaperService =
        UnsplashBaseApiService(httpClient)


    @Provides
    @Singleton
    fun provideUnsplashUserService(httpClient: HttpClient): UnsplashUserService =
        UnsplashBaseApiService(httpClient)
}
