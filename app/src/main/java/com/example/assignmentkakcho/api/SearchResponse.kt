package com.example.assignmentkakcho.api

import com.example.assignmentkakcho.data.model.Icon


data class SearchResponse(
    val icons: List<Icon>,
    val total_count: Int,
)
