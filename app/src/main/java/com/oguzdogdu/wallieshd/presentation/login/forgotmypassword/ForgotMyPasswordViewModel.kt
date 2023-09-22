package com.oguzdogdu.wallieshd.presentation.login.forgotmypassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.ForgotMyPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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
