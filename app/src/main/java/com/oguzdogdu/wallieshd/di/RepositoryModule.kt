package com.oguzdogdu.wallieshd.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oguzdogdu.data.di.Dispatcher
import com.oguzdogdu.data.di.WalliesDispatchers
import com.oguzdogdu.data.repository.AuthenticatorImpl
import com.oguzdogdu.data.repository.UnsplashUserRepositoryImpl
import com.oguzdogdu.data.repository.WallpaperRepositoryImpl
import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.repository.UnsplashUserRepository
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.network.service.UnsplashUserService
import com.oguzdogdu.network.service.WallpaperService
import com.oguzdogdu.wallieshd.cache.dao.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideWallpaperRepository(
        service: WallpaperService,
        dao: FavoriteDao,
        @Dispatcher(WalliesDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): WallpaperRepository {
        return WallpaperRepositoryImpl(service, dao, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): Authenticator {
        return AuthenticatorImpl(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideUnsplashUserRepository(service: UnsplashUserService): UnsplashUserRepository {
        return UnsplashUserRepositoryImpl(service)
    }
}
