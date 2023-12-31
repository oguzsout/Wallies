package com.oguzdogdu.wallieshd.presentation.authenticateduser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.GetCheckUserAuthStateUseCase
import com.oguzdogdu.domain.usecase.auth.GetCurrentUserInfoUseCase
import com.oguzdogdu.domain.usecase.auth.GetSignInCheckGoogleUseCase
import com.oguzdogdu.domain.usecase.auth.GetSignOutUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthenticedUserViewModel @Inject constructor(
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase,
    private val getSignOutUseCase: GetSignOutUseCase,
    private val getSignInCheckGoogleUseCase: GetSignInCheckGoogleUseCase,
    private val getCheckUserAuthStateUseCase: GetCheckUserAuthStateUseCase
) : ViewModel() {

    private val _userState: MutableStateFlow<AuthenticatedUserScreenState?> = MutableStateFlow(null)
    val userState = _userState.asStateFlow()

    fun handleUiEvents(event: AuthenticatedUserEvent) {
        when (event) {
            is AuthenticatedUserEvent.CheckUserAuth -> {
                checkSignIn()
            }
            is AuthenticatedUserEvent.FetchUserInfos -> {
                fetchUserDatas()
                checkUserSignInMethod()
            }

            is AuthenticatedUserEvent.SignOut -> {
                signOut()
            }
        }
    }

    private fun fetchUserDatas() {
        viewModelScope.launch {
            getCurrentUserInfoUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _userState.update { AuthenticatedUserScreenState.Loading }

                    is Resource.Error -> _userState.update {
                        AuthenticatedUserScreenState.UserInfoError(
                            result.errorMessage
                        )
                    }

                    is Resource.Success ->
                        _userState.update {
                            AuthenticatedUserScreenState.UserInfos(
                                name = result.data?.name,
                                surname = result.data?.surname,
                                email = result.data?.email,
                                profileImage = result.data?.image,
                                favorites = result.data?.favorites.orEmpty()
                            )
                        }

                    else -> {}
                }
            }
        }
    }

    private fun checkSignIn() {
        viewModelScope.launch {
            getCheckUserAuthStateUseCase.invoke().collectLatest { status ->
                _userState.update {
                    AuthenticatedUserScreenState.CheckUserAuthenticated(
                        status
                    )
                }
            }
        }
    }

    private fun checkUserSignInMethod() {
        viewModelScope.launch {
            getSignInCheckGoogleUseCase.invoke().collectLatest { result ->
                _userState.update {
                    AuthenticatedUserScreenState.CheckUserGoogleSignIn(
                        isAuthenticated = result
                    )
                }
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            getSignOutUseCase.invoke()
        }
    }
}
