package com.oguzdogdu.wallies.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.CheckUserAuthenticatedUseCase
import com.oguzdogdu.domain.usecase.auth.SignInUseCase
import com.oguzdogdu.domain.usecase.auth.SignOutUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val checkUserAuthenticatedUseCase: CheckUserAuthenticatedUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {
    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Start)
    val loginState = _loginState.asStateFlow()

    private val _signInStatus: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val signInStatus = _signInStatus.asStateFlow()

    init {
        checkSignIn()
    }

    private fun checkSignIn() {
        viewModelScope.launch {
            checkUserAuthenticatedUseCase.invoke().collect { status ->
                when (status) {
                    is Resource.Success -> {
                        if (status.data) {
                            _loginState.update { LoginState.UserSignIn }
                        } else {
                            _loginState.update { LoginState.UserNotSignIn }
                        }
                    }
                    is Resource.Error -> {}
                    else -> {}
                }
            }
        }
    }

    fun signIn(userEmail: String, password: String) = viewModelScope.launch {
        signInUseCase(userEmail, password).collect { response ->
            when (response) {
                is Resource.Success -> {
                    _loginState.update { LoginState.UserSignIn }
                }
                is Resource.Error -> {
                    _loginState.update { LoginState.ErrorSignIn(errorMessage = response.errorMessage) }
                }
                else -> {
                    _loginState.update { LoginState.Loading }
                }
            }
        }
    }
}