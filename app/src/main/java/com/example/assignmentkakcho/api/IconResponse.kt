package com.example.assignmentkakcho.api

import com.example.assignmentkakcho.data.model.Icon


data class IconResponse(
    val icons: List<Icon>,
    val total_count: Int,
)
