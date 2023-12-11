package com.oguzdogdu.wallieshd.presentation.authenticateduser.editpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.GetUpdatePasswordUseCase
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.wallieshd.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditPasswordViewModel @Inject constructor(
    private val getUpdatePasswordUseCase: GetUpdatePasswordUseCase
) : ViewModel() {

    private val _passwordState: MutableStateFlow<EditPasswordScreenState?> = MutableStateFlow(null)
    val passwordState = _passwordState.asStateFlow()

    fun handleUIEvent(event: EditPasswordScreenEvent) {
        when (event) {
            is EditPasswordScreenEvent.UserPassword -> {
                updatePassword(event.password)
            }
        }
    }
    private fun updatePassword(password: String?) {
        viewModelScope.launch {
            getUpdatePasswordUseCase.invoke(password = password).collectLatest { state ->
                when (state) {
                    is Resource.Loading -> {
                        _passwordState.update { EditPasswordScreenState.Loading }
                    }

                    is Resource.Error -> {
                        _passwordState.update {
                            EditPasswordScreenState.PasswordChangeError(
                                errorMessage = state.errorMessage
                            )
                        }
                    }

                    is Resource.Success -> {
                        _passwordState.update {
                            EditPasswordScreenState.PasswordChangeSucceed(
                                successMessage = R.string.password_change
                            )
                        }
                    }
                }
            }
        }
    }
}
