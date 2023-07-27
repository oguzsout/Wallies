package com.oguzdogdu.wallies.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.CheckUserAuthenticatedUseCase
import com.oguzdogdu.domain.usecase.auth.SignInUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val checkUserAuthenticatedUseCase: CheckUserAuthenticatedUseCase
) : ViewModel() {
    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Start)
    val loginState = _loginState.asStateFlow()

    private val _signInStatus: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val signInStatus = _signInStatus.asStateFlow()

    init {
        checkSignIn()
    }

    fun handleUIEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.UserSignIn -> {
                signIn(userEmail = event.email, password = event.password)
            }
        }
    }

    private fun checkSignIn() {
        viewModelScope.launch {
            checkUserAuthenticatedUseCase.invoke().collectLatest { status ->
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

    private fun signIn(userEmail: String, password: String) = viewModelScope.launch {
        signInUseCase(userEmail, password).collect { response ->
            when (response) {
                is Resource.Success -> {
                    _loginState.update { LoginState.UserSignIn }
                }
                is Resource.Error -> {
                    _loginState.update {
                        LoginState.ErrorSignIn(
                            errorMessage = response.errorMessage
                        )
                    }
                }
                else -> {
                    _loginState.update { LoginState.Loading }
                }
            }
        }
    }
}
