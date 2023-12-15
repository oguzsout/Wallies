package com.oguzdogdu.domain.di

import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.repository.UnsplashUserRepository
import com.oguzdogdu.domain.repository.WallpaperRepository
import com.oguzdogdu.domain.usecase.auth.GetChangeEmailUseCase
import com.oguzdogdu.domain.usecase.auth.GetChangeEmailUseCaseImpl
import com.oguzdogdu.domain.usecase.auth.GetChangeProfilePhotoUseCase
import com.oguzdogdu.domain.usecase.auth.GetChangeProfilePhotoUseCaseImpl
import com.oguzdogdu.domain.usecase.auth.GetChangeSurnameUseCase
import com.oguzdogdu.domain.usecase.auth.GetChangeSurnameUseCaseImpl
import com.oguzdogdu.domain.usecase.auth.GetChangeUsernameUseCase
import com.oguzdogdu.domain.usecase.auth.GetChangeUsernameUseCaseImpl
import com.oguzdogdu.domain.usecase.auth.GetCheckUserAuthStateUseCase
import com.oguzdogdu.domain.usecase.auth.GetCheckUserAuthStateUseCaseImpl
import com.oguzdogdu.domain.usecase.auth.GetCurrentUserInfoUseCase
import com.oguzdogdu.domain.usecase.auth.GetCurrentUserInfoUseCaseImpl
import com.oguzdogdu.domain.usecase.auth.GetDeleteFavoriteFromFirebaseUseCase
import com.oguzdogdu.domain.usecase.auth.GetDeleteFavoriteFromFirebaseUseCaseImpl
import com.oguzdogdu.domain.usecase.auth.GetFavoritesToFirebaseUseCase
import com.oguzdogdu.domain.usecase.auth.GetFavoritesToFirebaseUseCaseImpl
import com.oguzdogdu.domain.usecase.auth.GetForgotMyPasswordUseCase
import com.oguzdogdu.domain.usecase.auth.GetForgotMyPasswordUseCaseImpl
import com.oguzdogdu.domain.usecase.auth.GetSignInCheckGoogleUseCase
import com.oguzdogdu.domain.usecase.auth.GetSignInCheckGoogleUseCaseImpl
import com.oguzdogdu.domain.usecase.auth.GetSignInUseCase
import com.oguzdogdu.domain.usecase.auth.GetSignInUseCaseImpl
import com.oguzdogdu.domain.usecase.auth.GetSignInWithGoogleUseCase
import com.oguzdogdu.domain.usecase.auth.GetSignInWithGoogleUseCaseImpl
import com.oguzdogdu.domain.usecase.auth.GetSignOutUseCase
import com.oguzdogdu.domain.usecase.auth.GetSignOutUseCaseImpl
import com.oguzdogdu.domain.usecase.auth.GetSignUpUseCase
import com.oguzdogdu.domain.usecase.auth.GetSignUpUseCaseImpl
import com.oguzdogdu.domain.usecase.auth.GetUpdatePasswordUseCase
import com.oguzdogdu.domain.usecase.auth.GetUpdatePasswordUseCaseImpl
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
import com.oguzdogdu.domain.usecase.topics.GetTopicsListWithPagingUseCase
import com.oguzdogdu.domain.usecase.topics.GetTopicsListWithPagingUseCaseImpl
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

    @Provides
    fun provideAddFavoriteImageToFirebaseUseCase(
        repository: Authenticator
    ): GetFavoritesToFirebaseUseCase {
        return GetFavoritesToFirebaseUseCaseImpl(repository)
    }

    @Provides
    fun provideChangeEmailUseCase(
        repository: Authenticator
    ): GetChangeEmailUseCase {
        return GetChangeEmailUseCaseImpl(repository)
    }

    @Provides
    fun provideChangeSurnameUseCase(
        repository: Authenticator
    ): GetChangeSurnameUseCase {
        return GetChangeSurnameUseCaseImpl(repository)
    }

    @Provides
    fun provideChangeUsernameUseCase(
        repository: Authenticator
    ): GetChangeUsernameUseCase {
        return GetChangeUsernameUseCaseImpl(repository)
    }

    @Provides
    fun provideChangeProfilePhotoUseCase(
        repository: Authenticator
    ): GetChangeProfilePhotoUseCase {
        return GetChangeProfilePhotoUseCaseImpl(repository)
    }

    @Provides
    fun provideCheckGoogleSignInUseCase(
        repository: Authenticator
    ): GetSignInCheckGoogleUseCase {
        return GetSignInCheckGoogleUseCaseImpl(repository)
    }

    @Provides
    fun provideCheckUserAuthStateUseCase(
        repository: Authenticator
    ): GetCheckUserAuthStateUseCase {
        return GetCheckUserAuthStateUseCaseImpl(repository)
    }

    @Provides
    fun provideDeleteFavoriteFromFirebaseUseCase(
        repository: Authenticator
    ): GetDeleteFavoriteFromFirebaseUseCase {
        return GetDeleteFavoriteFromFirebaseUseCaseImpl(repository)
    }

    @Provides
    fun provideForgotMyPasswordUseCase(
        repository: Authenticator
    ): GetForgotMyPasswordUseCase {
        return GetForgotMyPasswordUseCaseImpl(repository)
    }

    @Provides
    fun provideCurrentUserInfoUseCase(
        repository: Authenticator
    ): GetCurrentUserInfoUseCase {
        return GetCurrentUserInfoUseCaseImpl(repository)
    }

    @Provides
    fun provideGetSignInUseCase(
        repository: Authenticator
    ): GetSignInUseCase {
        return GetSignInUseCaseImpl(repository)
    }

    @Provides
    fun provideGetSignInWithGoogleUseCase(
        repository: Authenticator
    ): GetSignInWithGoogleUseCase {
        return GetSignInWithGoogleUseCaseImpl(repository)
    }

    @Provides
    fun provideSignOutUseCase(
        repository: Authenticator
    ): GetSignOutUseCase {
        return GetSignOutUseCaseImpl(repository)
    }

    @Provides
    fun provideSignUpUseCase(
        repository: Authenticator
    ): GetSignUpUseCase {
        return GetSignUpUseCaseImpl(repository)
    }

    @Provides
    fun provideUpdatePasswordUseCase(
        repository: Authenticator
    ): GetUpdatePasswordUseCase {
        return GetUpdatePasswordUseCaseImpl(repository)
    }

    @Provides
    fun provideTopicsListPagingUseCase(
        repository: WallpaperRepository
    ): GetTopicsListWithPagingUseCase {
        return GetTopicsListWithPagingUseCaseImpl(repository)
    }
}