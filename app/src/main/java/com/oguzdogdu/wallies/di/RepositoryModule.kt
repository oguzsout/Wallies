package com.oguzdogdu.wallies.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oguzdogdu.data.repository.AuthenticatorImpl
import com.oguzdogdu.data.repository.WallpaperRepositoryImpl
import com.oguzdogdu.data.source.remote.WallpaperService
import com.oguzdogdu.domain.repository.Authenticator
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
    fun provideWallpaperRepository(
        service: WallpaperService,
        dao: FavoriteDao
    ): WallpaperRepository {
        return WallpaperRepositoryImpl(service, dao)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): Authenticator {
        return AuthenticatorImpl(auth, firestore)
    }
}
