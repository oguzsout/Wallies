package com.oguzdogdu.wallies.presentation.settings

sealed class SettingsEvent {
    data class SetNewTheme(val value: String) : SettingsEvent()
    object ThemeChanged : SettingsEvent()
    data class SetNewLanguage(val value: String) : SettingsEvent()
    object LanguageChanged : SettingsEvent()
}
