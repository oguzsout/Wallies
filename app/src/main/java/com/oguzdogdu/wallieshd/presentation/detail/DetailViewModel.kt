package com.oguzdogdu.wallieshd.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.model.favorites.FavoriteImages
import com.oguzdogdu.domain.usecase.auth.AddFavoritesToFirebaseUseCase
import com.oguzdogdu.domain.usecase.auth.CheckUserAuthenticatedUseCase
import com.oguzdogdu.domain.usecase.auth.DeleteFavoriteToFirebaseUseCase
import com.oguzdogdu.domain.usecase.auth.GetCurrentUserDatasUseCase
import com.oguzdogdu.domain.usecase.favorites.AddFavoritesUseCase
import com.oguzdogdu.domain.usecase.favorites.DeleteFavoritesUseCase
import com.oguzdogdu.domain.usecase.favorites.GetFavoritesUseCase
import com.oguzdogdu.domain.usecase.singlephoto.SinglePhotoUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val useCase: SinglePhotoUseCase,
    private val favoritesUseCase: AddFavoritesUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val deleteFavoritesUseCase: DeleteFavoritesUseCase,
    private val addFavoritesUseCase: AddFavoritesToFirebaseUseCase,
    private val deleteFavoriteToFirebaseUseCase: DeleteFavoriteToFirebaseUseCase,
    private val checkUserAuthenticatedUseCase: CheckUserAuthenticatedUseCase,
    private val getCurrentUserDatasUseCase: GetCurrentUserDatasUseCase
) : ViewModel() {

    private val _getPhoto = MutableStateFlow<DetailState?>(null)
    val photo = _getPhoto.asStateFlow()

    private val _toogleState = MutableStateFlow(false)
    val toggleState = _toogleState.asStateFlow()

    fun handleUIEvent(event: DetailScreenEvent) {
        when (event) {
            is DetailScreenEvent.GetPhotoDetails -> {
                getSinglePhoto(id = event.id)
            }

            is DetailScreenEvent.AddFavorites -> {
                addImagesToFavorites(
                    FavoriteImages(
                        id = event.photo?.id.orEmpty(),
                        url = event.photo?.urls.orEmpty(),
                        profileImage = event.photo?.profileimage.orEmpty(),
                        portfolioUrl = event.photo?.portfolio.orEmpty(),
                        name = event.photo?.username.orEmpty(),
                        isChecked = true
                    )

                )
            }

            is DetailScreenEvent.DeleteFavorites -> {
                deleteImagesToFavorites(
                    FavoriteImages(
                        id = event.photo?.id.orEmpty(),
                        url = event.photo?.urls.orEmpty(),
                        profileImage = event.photo?.profileimage.orEmpty(),
                        portfolioUrl = event.photo?.portfolio.orEmpty(),
                        name = event.photo?.username.orEmpty(),
                        isChecked = false
                    )
                )
            }
            is DetailScreenEvent.GetPhotoFromWhere -> {
                checkAuthStatusForShowFavorites(id = event.id, url = event.url)
            }
        }
    }

    private fun getSinglePhoto(id: String?) {
        viewModelScope.launch {
            useCase.invoke(id = id).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _getPhoto.update { DetailState.Loading }

                    is Resource.Success -> {
                        _getPhoto.update { DetailState.DetailOfPhoto(detail = result.data) }
                    }

                    is Resource.Error -> {
                        _getPhoto.update {
                            DetailState.DetailError(
                                errorMessage = result.errorMessage
                            )
                        }
                    }
                }
            }
        }
    }

    private fun addImagesToFavorites(
        favoriteImage: FavoriteImages
    ) {
        viewModelScope.launch {
            checkUserAuthenticatedUseCase.invoke().collectLatest { status ->
                when (status) {
                    is Resource.Success -> {
                        when (status.data) {
                            true -> addFavoritesUseCase.invoke(
                                id = favoriteImage.id,
                                favorite = favoriteImage.url
                            )
                            false -> favoritesUseCase.invoke(favoriteImage)
                        }
                    }

                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun deleteImagesToFavorites(
        favoriteImage: FavoriteImages
    ) {
        viewModelScope.launch {
            checkUserAuthenticatedUseCase.invoke().collectLatest { status ->
                when (status) {
                    is Resource.Success -> {
                        when (status.data) {
                            true -> deleteFavoriteToFirebaseUseCase.invoke(
                                id = favoriteImage.id,
                                favorite = favoriteImage.url
                            )
                            false -> deleteFavoritesUseCase.invoke(favoriteImage)
                        }
                    }

                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun checkAuthStatusForShowFavorites(id: String?, url: String?) {
        viewModelScope.launch {
            checkUserAuthenticatedUseCase.invoke().collectLatest { status ->
                when (status) {
                    is Resource.Success -> {
                        when (status.data) {
                            true -> getFavoritesFromFirebase(url)
                            false -> getFavoritesFromRoom(id)
                        }
                    }

                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun getFavoritesFromFirebase(url: String?) {
        viewModelScope.launch {
            getCurrentUserDatasUseCase.invoke().collectLatest { userDatas ->
                when (userDatas) {
                    is Resource.Success -> {
                        val containsUrl = userDatas.data.favorites.any { favorite ->
                            favorite.containsValue(url)
                        }
                        _toogleState.emit(containsUrl)
                        _getPhoto.update {
                            DetailState.FavoriteStateOfPhoto(
                                favorite = containsUrl
                            )
                        }
                    }

                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun getFavoritesFromRoom(id: String?) {
        viewModelScope.launch {
            getFavoritesUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
                    is Resource.Success -> {
                        val matchingFavorite = result.data.find { it.id == id }
                        _getPhoto.update {
                            matchingFavorite?.isChecked?.let { it1 ->
                                DetailState.FavoriteStateOfPhoto(
                                    favorite = it1
                                )
                            }
                        }
                        matchingFavorite?.isChecked?.let { _toogleState.emit(it) }
                    }
                }
            }
        }
    }
}
