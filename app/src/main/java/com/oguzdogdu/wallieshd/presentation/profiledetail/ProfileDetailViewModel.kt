package com.oguzdogdu.wallieshd.presentation.profiledetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.collection.GetUserCollectionUseCase
import com.oguzdogdu.domain.usecase.userdetails.GetUnsplashUserDetailsUseCase
import com.oguzdogdu.domain.usecase.userdetails.GetUnsplashUsersPhotosUseCase
import com.oguzdogdu.domain.wrapper.Resource
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

    private var username: String? = null

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
                when (result) {
                    is Resource.Success -> _getUserDetails.update {
                        ProfileDetailState.UserInfos(userDetails = result.data)
                    }

                    is Resource.Loading -> _getUserDetails.update { ProfileDetailState.Loading }
                    is Resource.Error -> _getUserDetails.update {
                        ProfileDetailState.UserDetailError(errorMessage = result.errorMessage)
                    }
                }
            }
        }
    }

    private fun getUsersPhotos(username: String?) {
        viewModelScope.launch {
            getUnsplashUsersPhotosUseCase.invoke(username).collectLatest { result ->
                when (result) {
                    is Resource.Success -> _getUserPhotoList.update {
                        UserPhotosState.UserPhotos(result.data)
                    }

                    is Resource.Loading -> _getUserPhotoList.update { UserPhotosState.Loading }
                    is Resource.Error -> _getUserPhotoList.update {
                        UserPhotosState.UserPhotosError(errorMessage = result.errorMessage)
                    }
                }
            }
        }
    }
    private fun getUsersCollections(username: String?) {
        viewModelScope.launch {
            getUserCollectionUseCase.invoke(username).collectLatest { result ->
                when (result) {
                    is Resource.Success -> _getUserCollectionList.update {
                        UserCollectionState.UserCollections(result.data)
                    }

                    is Resource.Loading -> _getUserCollectionList.update { UserCollectionState.Loading }
                    is Resource.Error -> _getUserCollectionList.update {
                        UserCollectionState.UserCollectionError(errorMessage = result.errorMessage)
                    }
                }
            }
        }
    }
}
