package com.oguzdogdu.domain.di

import com.oguzdogdu.domain.repository.UnsplashUserRepository
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.domain.usecase.collection.GetCollectionByLikesUseCase
import com.oguzdogdu.domain.usecase.collection.GetCollectionByLikesUseCaseImpl
import com.oguzdogdu.domain.usecase.collection.GetCollectionByTitlesUseCase
import com.oguzdogdu.domain.usecase.collection.GetCollectionByTitlesUseCaseImpl
import com.oguzdogdu.domain.usecase.collection.GetCollectionListByIdUseCase
import com.oguzdogdu.domain.usecase.collection.GetCollectionListByIdUseCaseImpl
import com.oguzdogdu.domain.usecase.collection.GetCollectionUseCase
import com.oguzdogdu.domain.usecase.collection.GetCollectionUseCaseImpl
import com.oguzdogdu.domain.usecase.collection.GetUserCollectionUseCase
import com.oguzdogdu.domain.usecase.collection.GetUserCollectionUseCaseImpl
import com.oguzdogdu.domain.usecase.home.GetPopularAndLatestUseCase
import com.oguzdogdu.domain.usecase.home.GetPopularAndLatestUseCaseImpl
import com.oguzdogdu.domain.usecase.latest.GetLatestUseCases
import com.oguzdogdu.domain.usecase.latest.GetLatestUseCasesImpl
import com.oguzdogdu.domain.usecase.popular.GetPopularUseCase
import com.oguzdogdu.domain.usecase.popular.GetPopularUseCaseImpl
import com.oguzdogdu.domain.usecase.topics.GetTopicsListUseCase
import com.oguzdogdu.domain.usecase.topics.GetTopicsListUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object WallpaperUseCaseModule {
    @Provides
    fun providePopularAndLatestUseCase(
        repository: WallpaperRepository
    ): GetPopularAndLatestUseCase {
        return GetPopularAndLatestUseCaseImpl(repository)
    }

    @Provides
    fun provideTopicsListUseCase(
        repository: WallpaperRepository
    ): GetTopicsListUseCase {
        return GetTopicsListUseCaseImpl(repository)
    }

    @Provides
    fun providePopularPagingListUseCase(
        repository: WallpaperRepository
    ): GetPopularUseCase {
        return GetPopularUseCaseImpl(repository)
    }
    @Provides
    fun provideLatestPagingListUseCase(
        repository: WallpaperRepository
    ): GetLatestUseCases {
        return GetLatestUseCasesImpl(repository)
    }

    @Provides
    fun provideCollectionByLikeUseCase(
        repository: WallpaperRepository
    ): GetCollectionByLikesUseCase {
        return GetCollectionByLikesUseCaseImpl(repository)
    }

    @Provides
    fun provideCollectionByTitlesUseCase(
        repository: WallpaperRepository
    ): GetCollectionByTitlesUseCase {
        return GetCollectionByTitlesUseCaseImpl(repository)
    }

    @Provides
    fun provideCollectionByIdUseCase(
        repository: WallpaperRepository
    ): GetCollectionListByIdUseCase {
        return GetCollectionListByIdUseCaseImpl(repository)
    }

    @Provides
    fun provideCollectionUseCase(
        repository: WallpaperRepository
    ): GetCollectionUseCase {
        return GetCollectionUseCaseImpl(repository)
    }

    @Provides
    fun provideUserCollectionUseCase(
        repository: UnsplashUserRepository
    ): GetUserCollectionUseCase {
        return GetUserCollectionUseCaseImpl(repository)
    }
}