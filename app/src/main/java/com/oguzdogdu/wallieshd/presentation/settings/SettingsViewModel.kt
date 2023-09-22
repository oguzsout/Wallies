package com.oguzdogdu.wallieshd.presentation.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.repository.DataStore
import com.oguzdogdu.domain.usecase.auth.SignOutUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    val showBottomNavigation = MutableLiveData(true)

    private val _themeState = MutableStateFlow<SettingsState.ThemeValue?>(null)
    val themeState = _themeState.asStateFlow()

    private val _languageState = MutableStateFlow<SettingsState.LanguageValue?>(null)
    val languageState = _languageState.asStateFlow()

    init {
        getThemeValue()
        getLanguageValue()
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
}
