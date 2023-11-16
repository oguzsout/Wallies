package com.oguzdogdu.wallieshd.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.model.favorites.FavoriteImages
import com.oguzdogdu.domain.repository.DataStore
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val dataStore: DataStore,
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
                addOrDeleteFavoritesToAnyDatabase(
                    FavoriteImages(
                        id = event.photo?.id.orEmpty(),
                        url = event.photo?.urls.orEmpty(),
                        profileImage = event.photo?.profileimage.orEmpty(),
                        portfolioUrl = event.photo?.portfolio.orEmpty(),
                        name = event.photo?.username.orEmpty(),
                        isChecked = true
                    ),
                    DatabaseProcess.ADD.name
                )
            }

            is DetailScreenEvent.DeleteFavorites -> {
                addOrDeleteFavoritesToAnyDatabase(
                    FavoriteImages(
                        id = event.photo?.id.orEmpty(),
                        url = event.photo?.urls.orEmpty(),
                        profileImage = event.photo?.profileimage.orEmpty(),
                        portfolioUrl = event.photo?.portfolio.orEmpty(),
                        name = event.photo?.username.orEmpty(),
                        isChecked = false
                    ),
                    DatabaseProcess.DELETE.name
                )
            }

            is DetailScreenEvent.GetPhotoFromWhere -> {
                checkAuthStatusForShowFavorites(id = event.id)
            }
            is DetailScreenEvent.SetLoginDialogState -> setUserAuthDialogPresent(event.isShown)
            DetailScreenEvent.CheckUserAuth -> checkUserAuthStatus()
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

    private fun checkUserAuthStatus() {
        viewModelScope.launch {
            checkUserAuthenticatedUseCase.invoke().collectLatest { status ->
                when (status) {
                    is Resource.Success -> {
                        _getPhoto.update { DetailState.UserAuthenticated(status.data) }
                    }

                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun setUserAuthDialogPresent(value: Boolean) = runBlocking {
        dataStore.setShowLoginWarningPresent(key = "isShow", value = value)
        getUserAuthDialogShown()
    }

    private fun getUserAuthDialogShown() {
        viewModelScope.launch {
            dataStore.getLoginWarningPresent("isShow").collect { result ->
                _getPhoto.update { DetailState.StateOfLoginDialog(isShown = result) }
            }
        }
    }

    private fun checkAuthStatusForShowFavorites(id: String?) {
        viewModelScope.launch {
            checkUserAuthenticatedUseCase.invoke().collectLatest { status ->
                when (status) {
                    is Resource.Success -> {
                        when (status.data) {
                            true -> getFavoritesFromFirebase(id)
                            false -> getFavoritesFromRoom(id)
                        }
                    }

                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun addOrDeleteFavoritesToAnyDatabase(favoriteImage: FavoriteImages, process: String?) {
        viewModelScope.launch {
            checkUserAuthenticatedUseCase.invoke().collectLatest { status ->
                when (status) {
                    is Resource.Success -> {
                        when (status.data) {
                            true -> {
                                when (process) {
                                    DatabaseProcess.ADD.name -> addImagesToFavorites(
                                        favoriteImage,
                                        whichDb = ChooseDB.FIREBASE.name
                                    )
                                    DatabaseProcess.DELETE.name -> deleteImagesToFavorites(
                                        favoriteImage,
                                        whichDb = ChooseDB.FIREBASE.name
                                    )
                                }
                            }
                            false -> {
                                when (process) {
                                    DatabaseProcess.ADD.name -> {
                                        addImagesToFavorites(
                                            favoriteImage,
                                            whichDb = ChooseDB.ROOM.name
                                        )
                                    }

                                    DatabaseProcess.DELETE.name -> deleteImagesToFavorites(
                                        favoriteImage,
                                        whichDb = ChooseDB.ROOM.name
                                    )
                                }
                            }
                        }
                    }

                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun addImagesToFavorites(
        favoriteImage: FavoriteImages,
        whichDb: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            when (whichDb) {
                ChooseDB.FIREBASE.name -> addFavoritesUseCase.invoke(
                    id = favoriteImage.id,
                    favorite = favoriteImage.url
                )

                ChooseDB.ROOM.name -> favoritesUseCase.invoke(favoriteImage)
            }
        }
    }

    private fun deleteImagesToFavorites(
        favoriteImage: FavoriteImages,
        whichDb: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            when (whichDb) {
                ChooseDB.FIREBASE.name -> {
                    deleteFavoriteToFirebaseUseCase.invoke(
                        id = favoriteImage.id,
                        favorite = favoriteImage.url
                    )
                }
                ChooseDB.ROOM.name -> deleteFavoritesUseCase.invoke(favoriteImage)
            }
        }
    }

    private fun getFavoritesFromFirebase(id: String?) {
        viewModelScope.launch {
            getCurrentUserDatasUseCase.invoke().collectLatest { userDatas ->
                when (userDatas) {
                    is Resource.Success -> {
                        val containsUrl = userDatas.data.favorites.any { favorite ->
                            favorite.containsValue(id)
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

    enum class ChooseDB(name: String) {
        FIREBASE("firebase"),
        ROOM("room")
    }
    enum class DatabaseProcess(name: String) {
        ADD("add"),
        DELETE("delete")
    }
}
