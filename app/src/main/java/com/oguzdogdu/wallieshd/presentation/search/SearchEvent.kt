package com.oguzdogdu.wallieshd.presentation.search

sealed class SearchEvent {
    data class EnteredSearchQuery(val query: String) : SearchEvent()
}
