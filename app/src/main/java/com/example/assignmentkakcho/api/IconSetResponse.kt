package com.example.assignmentkakcho.api

import com.example.assignmentkakcho.data.model.IconSet

data class IconSetResponse(
    val iconsets: List<IconSet>,
    val total_count: Int
)