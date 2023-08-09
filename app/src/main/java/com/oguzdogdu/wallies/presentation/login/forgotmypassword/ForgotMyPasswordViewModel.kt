package com.oguzdogdu.wallies.presentation.login.forgotmypassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.ForgotMyPasswordUseCase
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.wallies.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ForgotMyPasswordViewModel @Inject constructor(private val useCase: ForgotMyPasswordUseCase) : ViewModel() {


    fun handleUIEvent(event: ForgotPasswordScreenEvent) {
        when (event) {
            is ForgotPasswordScreenEvent.SendEmail -> {
                sendNewPasswordRequest(event.email)
            }
        }
    }

    private fun sendNewPasswordRequest(email: String?) {
        viewModelScope.launch {
            useCase.invoke(email = email)
        }
    }
}
