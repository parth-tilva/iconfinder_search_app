package com.example.assignmentkakcho.data.model.temp

import com.example.assignmentkakcho.data.model.Category

data class Iconset(
    val are_all_icons_glyph: Boolean,
    val author: Author,
    val categories: List<Category>,
    val icons_count: Int,
    val iconset_id: Int,
    val identifier: String,
    val is_premium: Boolean,
    val name: String,
    val prices: List<Price>,
    val published_at: String,
    val type: String
)