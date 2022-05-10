package com.example.assignmentkakcho.api

import com.example.assignmentkakcho.data.Icon


data class ApiResponse(
    val icons: List<Icon>,
    val total_count: Int,
)
