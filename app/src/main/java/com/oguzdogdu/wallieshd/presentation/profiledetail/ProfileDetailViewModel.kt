package com.oguzdogdu.wallieshd.presentation.profiledetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.collection.GetUserCollectionUseCase
import com.oguzdogdu.domain.usecase.userdetails.GetUnsplashUserDetailsUseCase
import com.oguzdogdu.domain.usecase.userdetails.GetUnsplashUsersPhotosUseCase
import com.oguzdogdu.domain.wrapper.onFailure
import com.oguzdogdu.domain.wrapper.onLoading
import com.oguzdogdu.domain.wrapper.onSuccess
import com.oguzdogdu.wallieshd.presentation.profiledetail.usercollections.UserCollectionState
import com.oguzdogdu.wallieshd.presentation.profiledetail.userphotos.UserPhotosState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val getUnsplashUserDetailsUseCase: GetUnsplashUserDetailsUseCase,
    private val getUnsplashUsersPhotosUseCase: GetUnsplashUsersPhotosUseCase,
    private val getUserCollectionUseCase: GetUserCollectionUseCase
) : ViewModel() {

    var username: String? = null
        private set

    fun setUsername(username: String?) {
        this.username = username
    }

    private val _getUserDetails = MutableStateFlow<ProfileDetailState?>(null)
    val getUserDetails = _getUserDetails.asStateFlow()

    private val _getUserPhotoList = MutableStateFlow<UserPhotosState?>(null)
    val getUserPhotoList = _getUserPhotoList.asStateFlow()

    private val _getUserCollectionList = MutableStateFlow<UserCollectionState?>(null)
    val getUserCollectionList = _getUserCollectionList.asStateFlow()

    fun handleUIEvent(event: ProfileDetailEvent) {
        when (event) {
            is ProfileDetailEvent.FetchUserDetailInfos -> getUserDetails(username)
            is ProfileDetailEvent.FetchUserPhotosList -> getUsersPhotos(username)
            is ProfileDetailEvent.FetchUserCollectionsList -> getUsersCollections(username)
        }
    }

    private fun getUserDetails(username: String?) {
        viewModelScope.launch {
            getUnsplashUserDetailsUseCase.invoke(username).collectLatest { result ->
                result.onLoading {
                    _getUserDetails.update { ProfileDetailState.Loading }
                }
                result.onSuccess { userDetails ->
                    _getUserDetails.update {
                        ProfileDetailState.UserInfos(userDetails = userDetails)
                    }
                }

                result.onFailure { error ->
                    _getUserDetails.update {
                        ProfileDetailState.UserDetailError(errorMessage = error)
                    }
                }
            }
        }
    }

    private fun getUsersPhotos(username: String?) {
        viewModelScope.launch {
            getUnsplashUsersPhotosUseCase.invoke(username).collectLatest { result ->
                result.onLoading {
                    _getUserPhotoList.update { UserPhotosState.Loading }
                }

                result.onSuccess { list ->
                    _getUserPhotoList.update {
                        UserPhotosState.UserPhotos(list)
                    }
                }

                result.onFailure { error ->
                    _getUserPhotoList.update {
                        UserPhotosState.UserPhotosError(errorMessage = error)
                    }
                }
            }
        }
    }

    private fun getUsersCollections(username: String?) {
        viewModelScope.launch {
            getUserCollectionUseCase.invoke(username).collectLatest { result ->
                result.onLoading {
                    _getUserCollectionList.update { UserCollectionState.Loading }
                }

                result.onSuccess { list ->
                    _getUserCollectionList.update {
                        UserCollectionState.UserCollections(list)
                    }
                }

                result.onFailure { error ->
                    _getUserCollectionList.update {
                        UserCollectionState.UserCollectionError(errorMessage = error)
                    }
                }
            }
        }
    }
}
