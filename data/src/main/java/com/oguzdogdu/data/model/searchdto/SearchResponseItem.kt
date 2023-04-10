package com.oguzdogdu.data.model.searchdto

data class SearchResponseItem(
    val results: List<Result>,
    val total: Int,
    val total_pages: Int
)