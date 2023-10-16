package com.oguzdogdu.wallieshd.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.CheckUserAuthenticatedUseCase
import com.oguzdogdu.domain.usecase.auth.GetCurrentUserDatasUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentUserDatasUseCase: GetCurrentUserDatasUseCase,
    private val userAuthenticatedUseCase: CheckUserAuthenticatedUseCase
) : ViewModel() {
    private val _userState = MutableStateFlow<MainScreenState?>(null)
    val userState = _userState.asStateFlow()

    fun handleUIEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.FetchMainScreenUserData -> {
                getUserProfileImage()
                checkUserAuthenticate()
            }
        }
    }

    private fun getUserProfileImage() {
        viewModelScope.launch {
            getCurrentUserDatasUseCase.invoke().collectLatest { value ->
                when (value) {
                    is Resource.Success -> {
                        _userState.update {
                            MainScreenState.UserProfile(
                                profileImage = value.data.image
                            )
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
                }
            }
        }
    }
    private fun checkUserAuthenticate(){
        viewModelScope.launch {
            userAuthenticatedUseCase.invoke().collectLatest { status ->
                when (status) {
                    is Resource.Success -> {
                        _userState.update {
                            MainScreenState.UserAuthenticated(isAuthenticated = status.data)
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
                }
            }
        }
    }
}
