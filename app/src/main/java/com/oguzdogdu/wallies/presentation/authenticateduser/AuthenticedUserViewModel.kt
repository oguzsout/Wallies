package com.oguzdogdu.wallies.presentation.authenticateduser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.CheckUserAuthenticatedUseCase
import com.oguzdogdu.domain.usecase.auth.GetCurrentUserDatasUseCase
import com.oguzdogdu.domain.usecase.auth.SignOutUseCase
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
    private val getCurrentUserDatasUseCase: GetCurrentUserDatasUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {

    private val _userState: MutableStateFlow<AuthenticatedUserScreenState?> = MutableStateFlow(null)
    val userState = _userState.asStateFlow()

    init {
        fetchUserDatas()
    }

    fun handleUiEvents(event: AuthenticatedUserEvent) {
        when (event) {
            is AuthenticatedUserEvent.SignOut -> {
                signOut()
            }
        }
    }

    private fun fetchUserDatas() {
        viewModelScope.launch {
            getCurrentUserDatasUseCase.invoke().collectLatest { result ->

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
                                name = result.data.name,
                                surname = result.data.surname,
                                email = result.data.email,
                                profileImage = result.data.image
                            )
                        }
                }
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            signOutUseCase.invoke()
        }
    }
}
