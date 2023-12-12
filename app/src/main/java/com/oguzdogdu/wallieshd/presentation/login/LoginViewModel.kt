package com.oguzdogdu.wallieshd.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.GetCheckUserAuthStateUseCase
import com.oguzdogdu.domain.usecase.auth.GetSignInUseCase
import com.oguzdogdu.domain.usecase.auth.GetSignInWithGoogleUseCase
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.wallieshd.util.FieldValidators
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getSignInUseCase: GetSignInUseCase,
    private val getCheckUserAuthStateUseCase: GetCheckUserAuthStateUseCase,
    private val getSignInWithGoogleUseCase: GetSignInWithGoogleUseCase
) : ViewModel() {

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Start)
    val loginState = _loginState.asStateFlow()

    private val userEmail = MutableStateFlow("")

    private val userPassword = MutableStateFlow("")

    init {
        checkSignIn()
    }

    fun handleUIEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.ButtonState -> {
                buttonStateUpdate()
            }
            is LoginScreenEvent.UserSignIn -> {
                signIn(userEmail = event.email, password = event.password)
            }
            is LoginScreenEvent.GoogleButton -> {
                signInWithGoogle(idToken = event.idToken)
            }
        }
    }

    fun setEmail(email: String?) {
        email?.let {
            userEmail.value = it
        }
    }

    fun setPassword(password: String?) {
        password?.let {
            userPassword.value = it
        }
    }

    private fun checkButtonState(): Boolean {
        return FieldValidators.isValidEmailCheck(input = userEmail.value) && FieldValidators.isValidPasswordCheck(
            input = userPassword.value
        )
    }

    private fun buttonStateUpdate() {
        viewModelScope.launch {
            val state = checkButtonState()
            _loginState.update { LoginState.ButtonEnabled(isEnabled = state) }
        }
    }

    private fun signInWithGoogle(idToken: String?) {
        viewModelScope.launch {
            getSignInWithGoogleUseCase.invoke(idToken).collectLatest { response ->
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

    private fun checkSignIn() {
        viewModelScope.launch {
            getCheckUserAuthStateUseCase.invoke().collectLatest { status ->
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

    private fun signIn(userEmail: String?, password: String?) = viewModelScope.launch {
        getSignInUseCase(userEmail, password).collectLatest { response ->
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
