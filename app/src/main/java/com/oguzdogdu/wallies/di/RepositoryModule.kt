package com.oguzdogdu.wallies.di

import com.oguzdogdu.data.repository.WallpaperRepositoryImpl
import com.oguzdogdu.data.source.WallpaperService
import com.oguzdogdu.domain.WallpaperRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(service: WallpaperService): WallpaperRepository {
        return WallpaperRepositoryImpl(service)
    }
}