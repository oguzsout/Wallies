package com.oguzdogdu.wallieshd.presentation.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.repository.DataStore
import com.oguzdogdu.domain.usecase.auth.CheckUserAuthenticatedUseCase
import com.oguzdogdu.domain.usecase.auth.SignOutUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore,
    private val checkUserAuthenticatedUseCase: CheckUserAuthenticatedUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    val showBottomNavigation = MutableLiveData(true)
    val checkSignIn = MutableStateFlow(false)

    private var _isStartDestinationChanged: MutableStateFlow<SettingsState.FirstOpened> =
        MutableStateFlow(SettingsState.FirstOpened(firstOpened = null))
    val isStartDestinationChanged = _isStartDestinationChanged.asStateFlow()

    private val _themeState = MutableStateFlow<SettingsState.ThemeValue?>(null)
    val themeState = _themeState.asStateFlow()

    private val _languageState = MutableStateFlow<SettingsState.LanguageValue?>(null)
    val languageState = _languageState.asStateFlow()

    init {
        getDestination()
        getThemeValue()
        getLanguageValue()
    }

    private fun getDestination() {
        viewModelScope.launch {
            dataStore.getAppFirstOpen().collectLatest { dest ->
                _isStartDestinationChanged.update {
                    SettingsState.FirstOpened(
                        firstOpened = dest
                    )
                }
            }
        }
    }

    fun handleUIEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SetNewTheme -> {
                setThemeValue(event.value)
            }

            is SettingsEvent.ThemeChanged -> {
                getThemeValue()
            }

            is SettingsEvent.LanguageChanged -> {
                getLanguageValue()
            }

            is SettingsEvent.SetNewLanguage -> {
                setLanguageValue(event.value)
            }
        }
    }

    private fun setThemeValue(value: String) = runBlocking {
        dataStore.putThemeStrings(key = "theme", value = value)
    }

    private fun getThemeValue() {
        viewModelScope.launch {
            dataStore.getThemeStrings(key = "theme").collect { value ->
                when (value) {
                    is Resource.Success -> {
                        _themeState.update {
                            SettingsState.ThemeValue(value.data.orEmpty())
                        }
                    }

                    is Resource.Error -> {
                    }

                    else -> {
                    }
                }
            }
        }
    }

    private fun setLanguageValue(value: String) = runBlocking {
        dataStore.putLanguageStrings(key = "language", value = value)
    }

    private fun getLanguageValue() {
        viewModelScope.launch {
            dataStore.getLanguageStrings(key = "language").collect { value ->
                when (value) {
                    is Resource.Success -> {
                        _languageState.update {
                            SettingsState.LanguageValue(value.data.orEmpty())
                        }
                    }

                    is Resource.Error -> {
                    }

                    else -> {
                    }
                }
            }
        }
    }

    fun checkSignIn() {
        viewModelScope.launch {
            checkUserAuthenticatedUseCase.invoke().collectLatest { status ->
                when (status) {
                    is Resource.Success -> {
                        checkSignIn.value = status.data
                    }

                    is Resource.Error -> {}
                    else -> {}
                }
            }
        }
    }
}