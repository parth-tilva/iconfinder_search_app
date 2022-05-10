package com.example.assignmentkakcho.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.assignmentkakcho.api.IconfinderApi
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class IconfinderRepository @Inject constructor(private val iconfinderApi: IconfinderApi) {

    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSource(iconfinderApi, query) }
        ).liveData
}