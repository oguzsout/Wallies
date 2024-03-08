package com.oguzdogdu.wallieshd.presentation.search

sealed class SearchEvent {
    data class EnteredSearchQuery(
        val query: String?,
        val language: String?
    ) : SearchEvent()
    data object GetAppLanguageValue : SearchEvent()
}
