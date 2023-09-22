package com.oguzdogdu.wallieshd.presentation.settings

sealed class SettingsState {
    object Start : SettingsState()
    object SignOut : SettingsState()
    data class ThemeValue(val value: String) : SettingsState()
    data class LanguageValue(val value: String) : SettingsState()
    data class UserInfos(val name: String?, val surname: String?, val imageUrl: String?)
}
