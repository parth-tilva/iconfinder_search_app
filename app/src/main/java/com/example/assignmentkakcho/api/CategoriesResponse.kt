package com.example.assignmentkakcho.api

import com.example.assignmentkakcho.data.model.Category

data class CategoriesResponse(
    val categories: List<Category>,
    val total_count: Int
)