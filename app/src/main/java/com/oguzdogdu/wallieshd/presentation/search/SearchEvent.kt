package com.oguzdogdu.wallieshd.presentation.search

sealed class SearchEvent {
    data class EnteredSearchQuery(
        val query: String?,
        val language: String?,
        val position: Int? = null
    ) : SearchEvent()
    data object GetAppLanguageValue : SearchEvent()
}
