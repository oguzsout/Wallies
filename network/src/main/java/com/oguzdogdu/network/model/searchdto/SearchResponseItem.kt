package com.oguzdogdu.network.model.searchdto

data class SearchResponseItem(
    val results: List<Result>,
    val total: Int,
    val total_pages: Int
)