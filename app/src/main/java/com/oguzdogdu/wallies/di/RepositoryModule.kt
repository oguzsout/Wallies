package com.oguzdogdu.wallies.di

import com.oguzdogdu.data.repository.WallpaperRepositoryImpl
import com.oguzdogdu.data.source.remote.WallpaperService
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.wallies.cache.dao.FavoriteDao
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
    fun provideRepository(service: WallpaperService, dao: FavoriteDao): WallpaperRepository {
        return WallpaperRepositoryImpl(service, dao)
    }
}