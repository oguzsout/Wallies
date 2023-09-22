package com.oguzdogdu.wallieshd.cache.di

import android.content.Context
import com.oguzdogdu.wallieshd.cache.dao.FavoriteDao
import com.oguzdogdu.wallieshd.cache.database.FavoritesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): FavoritesDatabase {
        return FavoritesDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideDao(database: FavoritesDatabase): FavoriteDao {
        return database.favoritesDao
    }
}