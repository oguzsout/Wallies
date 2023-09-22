package com.oguzdogdu.wallieshd.presentation.profiledetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.userdetails.GetUnsplashUserDetailsUseCase
import com.oguzdogdu.domain.usecase.userdetails.GetUnsplashUsersPhotosUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val userDetailsUseCase: GetUnsplashUserDetailsUseCase,
    private val unsplashUsersPhotos: GetUnsplashUsersPhotosUseCase
) : ViewModel() {

    private val _getUserDetails = MutableStateFlow<ProfileDetailState?>(null)
    val getUserDetails = _getUserDetails.asStateFlow()

    fun handleUIEvent(event: ProfileDetailEvent) {
        when (event) {
            is ProfileDetailEvent.FetchUserDetailInfos -> getUserDetails(event.username)
            is ProfileDetailEvent.FetchUserCollections -> getUsersPhotos(event.username)
        }
    }

    private fun getUserDetails(username: String?) {
        viewModelScope.launch {
            userDetailsUseCase.invoke(username).collectLatest { result ->
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
            unsplashUsersPhotos.invoke(username).collectLatest { result ->
                when (result) {
                    is Resource.Success -> _getUserDetails.update {
                        ProfileDetailState.UserCollections(usersPhotos = result.data)
                    }

                    is Resource.Loading -> _getUserDetails.update { ProfileDetailState.Loading }
                    is Resource.Error -> _getUserDetails.update {
                        ProfileDetailState.UserCollectionsError(errorMessage = result.errorMessage)
                    }
                }
            }
        }
    }
}
