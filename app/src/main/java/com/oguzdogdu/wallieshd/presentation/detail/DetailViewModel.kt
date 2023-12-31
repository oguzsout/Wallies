package com.oguzdogdu.wallieshd.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.model.favorites.FavoriteImages
import com.oguzdogdu.domain.repository.DataStore
import com.oguzdogdu.domain.usecase.auth.GetCheckUserAuthStateUseCase
import com.oguzdogdu.domain.usecase.auth.GetCurrentUserInfoUseCase
import com.oguzdogdu.domain.usecase.auth.GetDeleteFavoriteFromFirebaseUseCase
import com.oguzdogdu.domain.usecase.auth.GetFavoritesToFirebaseUseCase
import com.oguzdogdu.domain.usecase.favorites.GetAddFavoritesUseCase
import com.oguzdogdu.domain.usecase.favorites.GetDeleteFromFavoritesUseCase
import com.oguzdogdu.domain.usecase.favorites.GetImageFromFavoritesUseCase
import com.oguzdogdu.domain.usecase.singlephoto.GetPhotoDetailUseCase
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.wrapper.onFailure
import com.oguzdogdu.domain.wrapper.onLoading
import com.oguzdogdu.domain.wrapper.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val dataStore: DataStore,
    private val getPhotoDetailUseCase: GetPhotoDetailUseCase,
    private val getAddFavoritesUseCase: GetAddFavoritesUseCase,
    private val getImageFromFavoritesUseCase: GetImageFromFavoritesUseCase,
    private val getDeleteFromFavoritesUseCase: GetDeleteFromFavoritesUseCase,
    private val getFavoritesToFirebaseUseCase: GetFavoritesToFirebaseUseCase,
    private val getDeleteFavoriteFromFirebaseUseCase: GetDeleteFavoriteFromFirebaseUseCase,
    private val getCheckUserAuthStateUseCase: GetCheckUserAuthStateUseCase,
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase
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
            getPhotoDetailUseCase.invoke(id = id).collectLatest { result ->
                result.onLoading {
                    _getPhoto.update { DetailState.Loading }
                }

                result.onSuccess { photo ->
                    _getPhoto.update { DetailState.DetailOfPhoto(detail = photo) }
                }

                result.onFailure { error ->
                    _getPhoto.update {
                        DetailState.DetailError(
                            errorMessage = error
                        )
                    }
                }
            }
        }
    }

    private fun checkUserAuthStatus() {
        viewModelScope.launch {
            getCheckUserAuthStateUseCase.invoke().collectLatest { status ->
                _getPhoto.update { DetailState.UserAuthenticated(status) }
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
            getCheckUserAuthStateUseCase.invoke().collectLatest { status ->
                when (status) {
                    true -> getFavoritesFromFirebase(id)
                    false -> getFavoritesFromRoom(id)
                }
            }
        }
    }

    private fun addOrDeleteFavoritesToAnyDatabase(favoriteImage: FavoriteImages, process: String?) {
        viewModelScope.launch {
            getCheckUserAuthStateUseCase.invoke().collectLatest { status ->
                when (status) {
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
        }
    }

    private fun addImagesToFavorites(
        favoriteImage: FavoriteImages,
        whichDb: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            when (whichDb) {
                ChooseDB.FIREBASE.name -> getFavoritesToFirebaseUseCase.invoke(
                    id = favoriteImage.id,
                    favorite = favoriteImage.url
                )

                ChooseDB.ROOM.name -> getAddFavoritesUseCase.invoke(favoriteImage)
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
                    getDeleteFavoriteFromFirebaseUseCase.invoke(
                        id = favoriteImage.id,
                        favorite = favoriteImage.url
                    )
                }
                ChooseDB.ROOM.name -> getDeleteFromFavoritesUseCase.invoke(favoriteImage)
            }
        }
    }

    private fun getFavoritesFromFirebase(id: String?) {
        viewModelScope.launch {
            getCurrentUserInfoUseCase.invoke().collectLatest { userDatas ->
                when (userDatas) {
                    is Resource.Success -> {
                        val containsUrl = userDatas.data?.favorites?.any { favorite ->
                            favorite?.containsValue(id) == true
                        }
                        containsUrl?.let { _toogleState.emit(it) }
                        _getPhoto.update {
                            DetailState.FavoriteStateOfPhoto(
                                favorite = containsUrl == true
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
            getImageFromFavoritesUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
                    is Resource.Success -> {
                        val matchingFavorite = result.data?.find { it.id == id }
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
