package com.oguzdogdu.wallies.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.SignUpUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpUseCase: SignUpUseCase) : ViewModel() {
    private val _signUpState: MutableStateFlow<SignUpState> = MutableStateFlow(SignUpState.Start)
    val signUpState = _signUpState.asStateFlow()

    fun userSignUp(name: String, surname: String, email: String, password: String) {
        viewModelScope.launch {
            signUpUseCase.invoke(
                user = com.oguzdogdu.domain.model.auth.User(
                    name = name,
                    surname = surname,
                    email = email
                ),
                password = password
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _signUpState.update { SignUpState.Loading }
                    }
                    is Resource.Error -> {
                        _signUpState.update { SignUpState.ErrorSignUp(result.errorMessage) }
                    }
                    is Resource.Success -> {
                        _signUpState.update { SignUpState.UserSignUp }
                    }
                }
            }
        }
    }
}
