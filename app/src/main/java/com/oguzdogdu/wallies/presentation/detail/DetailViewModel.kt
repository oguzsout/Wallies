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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val useCase: SinglePhotoUseCase,
    private val favoritesUseCase: AddFavoritesUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val deleteFavoritesUseCase: DeleteFavoritesUseCase
) : ViewModel() {

    private val _getPhoto = MutableStateFlow(DetailState())
    val photo = _getPhoto.asStateFlow()

    private val _favorites = MutableStateFlow(FavoriteState())
    val favorites = _favorites.asStateFlow()

    private val _eventChannel = Channel<DetailScreenEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    init {
        getFavorites()
    }

    fun handleUIEvent(event: DetailScreenEvent) {
        when (event) {
            is DetailScreenEvent.AddFavorites -> {
                addImagesToFavorites(
                    FavoriteImages(
                        id = photo.value.detail?.id ?: "",
                        url = photo.value.detail?.urls ?: "",
                        profileImage = photo.value.detail?.profileimage ?: "",
                        portfolioUrl = photo.value.detail?.portfolio ?: "",
                        name = photo.value.detail?.username ?: "",
                        isChecked = true
                    )
                )
            }

            is DetailScreenEvent.DeleteFavorites -> {
                deleteImagesToFavorites(
                    FavoriteImages(
                        id = photo.value.detail?.id ?: "",
                        url = photo.value.detail?.urls ?: "",
                        profileImage = photo.value.detail?.profileimage ?: "",
                        portfolioUrl = photo.value.detail?.portfolio ?: "",
                        name = photo.value.detail?.username ?: "",
                        isChecked = false
                    )
                )
            }
        }
    }

    fun getSinglePhoto(id: String) {
        useCase(id).onEach { result ->
            when (result) {
                is Resource.Loading -> _getPhoto.value = DetailState(isLoading = true)

                is Resource.Success -> {
                    result.data.let {
                        _getPhoto.value = DetailState(detail = it)
                    }
                }

                is Resource.Error -> {
                    _getPhoto.value = DetailState(error = result.errorMessage)
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
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

    private fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            getFavoritesUseCase.invoke().collectLatest { result ->
                result.let {
                    _favorites.value = FavoriteState(favorites = it)
                }
            }
        }
    }
}
