package com.oguzdogdu.wallieshd.presentation.authenticateduser.editemail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.GetChangeEmailUseCase
import com.oguzdogdu.domain.usecase.auth.GetCurrentUserInfoUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditEmailViewModel @Inject constructor(
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase,
    private val getChangeEmailUseCase: GetChangeEmailUseCase
) : ViewModel() {

    private val _emailState: MutableStateFlow<EditEmailScreenState?> = MutableStateFlow(
        null
    )
    val emailState = _emailState.asStateFlow()

    init {
        fetchAuthenticatedUserEmail()
    }

    fun handleUIEvent(event: EditUserEmailEvent) {
        when (event) {
            is EditUserEmailEvent.ChangedEmail -> {
                event.email?.let { changeEmail(email = it, password = event.password) }
                fetchAuthenticatedUserEmail()
            }
        }
    }

    private fun fetchAuthenticatedUserEmail() {
        viewModelScope.launch {
            getCurrentUserInfoUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _emailState.update { EditEmailScreenState.Loading }

                    is Resource.Error -> _emailState.update {
                        EditEmailScreenState.UserInfoError(
                            result.errorMessage
                        )
                    }

                    is Resource.Success -> _emailState.update {
                        EditEmailScreenState.UserEmail(
                            email = result.data?.email
                        )
                    }
                }
            }
        }
    }

    private fun changeEmail(email: String?, password: String?) {
        viewModelScope.launch {
            getChangeEmailUseCase.invoke(email = email, password = password).collect { result ->
                when (result) {
                    is Resource.Error -> _emailState.update {
                        EditEmailScreenState.UserInfoError(
                            result.errorMessage
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}
