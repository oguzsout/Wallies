package com.oguzdogdu.wallies.presentation.settings

sealed class SettingsState {
    object Start : SettingsState()
    object SignOut : SettingsState()
    data class ThemeValue(val value: String) : SettingsState()
    data class LanguageValue(val value: String) : SettingsState()
}
