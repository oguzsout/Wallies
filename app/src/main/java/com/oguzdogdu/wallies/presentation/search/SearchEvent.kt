package com.oguzdogdu.wallies.presentation.search

sealed class SearchEvent {
    data class EnteredSearchQuery(val query: String) : SearchEvent()
}
