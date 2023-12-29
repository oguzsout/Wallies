package com.oguzdogdu.wallieshd.presentation.settings

sealed class SettingsState {
    data class ThemeValue(val value: String) : SettingsState()
    data class LanguageValue(val value: String) : SettingsState()
    data class UserInfos(val name: String?, val surname: String?, val imageUrl: String?)
    data class FirstOpened(val firstOpened: Boolean?) : SettingsState()
}
