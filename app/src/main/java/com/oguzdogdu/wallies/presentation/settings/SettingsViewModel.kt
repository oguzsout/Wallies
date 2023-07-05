package com.oguzdogdu.wallies.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(private val signOutUseCase: SignOutUseCase) : ViewModel() {
    private val _loginState: MutableStateFlow<SettingsState> = MutableStateFlow(
        SettingsState.SignOut
    )
    val loginState = _loginState.asStateFlow()

    fun signOut() {
        viewModelScope.launch {
            _loginState.update { SettingsState.SignOut }
            signOutUseCase.invoke()
        }
    }
}
