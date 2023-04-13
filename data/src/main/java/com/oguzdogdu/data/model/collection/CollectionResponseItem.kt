package com.oguzdogdu.data.model.collection

import com.oguzdogdu.data.model.searchdto.Result

data class CollectionResponseItem(
    val results: List<CollectionResponse>,
    val total: Int,
    val total_pages: Int
)
