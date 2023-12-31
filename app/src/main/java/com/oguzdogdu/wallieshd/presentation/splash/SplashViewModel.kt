package com.oguzdogdu.wallieshd.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.auth.GetCheckUserAuthStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getCheckUserAuthStateUseCase: GetCheckUserAuthStateUseCase
) :
    ViewModel() {

    private val _splashState: MutableStateFlow<SplashScreenState> = MutableStateFlow(
        SplashScreenState.StartFlow
    )
    val splashState = _splashState.asStateFlow()

    init {
        checkSignIn()
    }

    private fun checkSignIn() {
        viewModelScope.launch {
            getCheckUserAuthStateUseCase.invoke().collectLatest { status ->
                if (status) {
                    _splashState.update { SplashScreenState.UserSignedIn }
                } else {
                    _splashState.update { SplashScreenState.UserNotSigned }
                }
            }
        }
    }
}
