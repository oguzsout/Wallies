package com.oguzdogdu.wallieshd.presentation.login.forgotmypassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.ForgotMyPasswordUseCase
import com.oguzdogdu.wallieshd.util.FieldValidators
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ForgotMyPasswordViewModel @Inject constructor(private val useCase: ForgotMyPasswordUseCase) : ViewModel() {

    private val _forgotPasswordState: MutableStateFlow<ForgotPasswordScreenState?> = MutableStateFlow(
        null
    )
    val forgotPasswordState = _forgotPasswordState.asStateFlow()

    private val userEmail = MutableStateFlow("")

    fun handleUIEvent(event: ForgotPasswordScreenEvent) {
        when (event) {
            is ForgotPasswordScreenEvent.SendEmail -> {
                sendNewPasswordRequest(event.email)
            }
            is ForgotPasswordScreenEvent.ButtonState -> {
                buttonStateUpdate()
            }
        }
    }

    fun setEmail(email: String?) {
        email?.let {
            userEmail.value = it
        }
    }

    private fun sendNewPasswordRequest(email: String?) {
        viewModelScope.launch {
            useCase.invoke(email = email)
        }
    }

    private fun checkButtonState(): Boolean {
        return FieldValidators.isValidEmailCheck(input = userEmail.value)
    }

    private fun buttonStateUpdate() {
        viewModelScope.launch {
            val state = checkButtonState()
            _forgotPasswordState.update { ForgotPasswordScreenState.ButtonEnabled(isEnabled = state) }
        }
    }
}
