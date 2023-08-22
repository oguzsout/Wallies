package com.oguzdogdu.wallies.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.model.favorites.FavoriteImages
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
    private val deleteFavoritesUseCase: DeleteFavoritesUseCase
) : ViewModel() {

    private val _getPhoto = MutableStateFlow<DetailState?>(null)
    val photo = _getPhoto.asStateFlow()

    fun handleUIEvent(event: DetailScreenEvent) {
        when (event) {
            is DetailScreenEvent.GetPhotoDetails -> {
                getSinglePhoto(id = event.id)
                getFavorite(id = event.id)
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

    private fun addImagesToFavorites(favoriteImage: FavoriteImages) {
        viewModelScope.launch {
            favoritesUseCase.invoke(favoriteImage)
        }
    }

    private fun deleteImagesToFavorites(favoriteImage: FavoriteImages) {
        viewModelScope.launch {
            deleteFavoritesUseCase.invoke(favoriteImage)
        }
    }

    private fun getFavorite(id: String?) {
        viewModelScope.launch {
            getFavoritesUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
                    is Resource.Success -> {
                        val matchingFavorite = result.data.find { it.id == id }
                        _getPhoto.update {
                            DetailState.FavoriteStateOfPhoto(
                                favorite = matchingFavorite?.isChecked
                            )
                        }
                    }
                }
            }
        }
    }
}
