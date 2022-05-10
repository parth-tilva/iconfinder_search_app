package com.example.assignmentkakcho.data

import android.os.Parcelable



data class Icon(
    val raster_sizes: List<Size>,
    val is_premium: Boolean,
    val tags: List<String>,
    val icon_id: Int,
    val prices: List<Price>
)  {

    data class Size(
        val size: Int,
        val formats: List<Format>,
    )

    data class Format(
        val format: String,
        val download_url: String,
        val preview_url: String,
    )

    data class Price(
        val price: Float,
        val currency: String
    )

}