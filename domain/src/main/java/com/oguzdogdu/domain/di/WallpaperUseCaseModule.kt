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
import com.oguzdogdu.domain.usecase.favorites.GetAddFavoritesUseCase
import com.oguzdogdu.domain.usecase.favorites.GetAddFavoritesUseCaseImpl
import com.oguzdogdu.domain.usecase.favorites.GetDeleteFromFavoritesUseCase
import com.oguzdogdu.domain.usecase.favorites.GetDeleteFromFavoritesUseCaseImpl
import com.oguzdogdu.domain.usecase.favorites.GetImageFromFavoritesUseCase
import com.oguzdogdu.domain.usecase.favorites.GetImageFromFavoritesUseCaseImpl
import com.oguzdogdu.domain.usecase.home.GetPopularAndLatestUseCase
import com.oguzdogdu.domain.usecase.home.GetPopularAndLatestUseCaseImpl
import com.oguzdogdu.domain.usecase.latest.GetLatestUseCases
import com.oguzdogdu.domain.usecase.latest.GetLatestUseCasesImpl
import com.oguzdogdu.domain.usecase.popular.GetPopularUseCase
import com.oguzdogdu.domain.usecase.popular.GetPopularUseCaseImpl
import com.oguzdogdu.domain.usecase.search.GetSearchPhotosUseCase
import com.oguzdogdu.domain.usecase.search.GetSearchPhotosUseCaseImpl
import com.oguzdogdu.domain.usecase.singlephoto.GetPhotoDetailUseCase
import com.oguzdogdu.domain.usecase.singlephoto.GetPhotoDetailUseCaseImpl
import com.oguzdogdu.domain.usecase.topics.GetTopicsListUseCase
import com.oguzdogdu.domain.usecase.topics.GetTopicsListUseCaseImpl
import com.oguzdogdu.domain.usecase.userdetails.GetUnsplashUserDetailsUseCase
import com.oguzdogdu.domain.usecase.userdetails.GetUnsplashUserDetailsUseCaseImpl
import com.oguzdogdu.domain.usecase.userdetails.GetUnsplashUsersPhotosUseCase
import com.oguzdogdu.domain.usecase.userdetails.GetUnsplashUsersPhotosUseCaseImpl
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

    @Provides
    fun provideUserDetailUseCase(
        repository: UnsplashUserRepository
    ): GetUnsplashUserDetailsUseCase {
        return GetUnsplashUserDetailsUseCaseImpl(repository)
    }

    @Provides
    fun provideUserPhotosUseCase(
        repository: UnsplashUserRepository
    ): GetUnsplashUsersPhotosUseCase {
        return GetUnsplashUsersPhotosUseCaseImpl(repository)
    }

    @Provides
    fun providePhotoDetailUseCase(
        repository: WallpaperRepository
    ): GetPhotoDetailUseCase {
        return GetPhotoDetailUseCaseImpl(repository)
    }

    @Provides
    fun provideSearchPhotosUseCase(
        repository: WallpaperRepository
    ): GetSearchPhotosUseCase {
        return GetSearchPhotosUseCaseImpl(repository)
    }

    @Provides
    fun provideFavoriteImageFromRoomUseCase(
        repository: WallpaperRepository
    ): GetImageFromFavoritesUseCase {
        return GetImageFromFavoritesUseCaseImpl(repository)
    }

    @Provides
    fun provideDeleteFavoriteImageFromRoomUseCase(
        repository: WallpaperRepository
    ): GetDeleteFromFavoritesUseCase {
        return GetDeleteFromFavoritesUseCaseImpl(repository)
    }

    @Provides
    fun provideAddFavoriteImageToRoomUseCase(
        repository: WallpaperRepository
    ): GetAddFavoritesUseCase {
        return GetAddFavoritesUseCaseImpl(repository)
    }
}