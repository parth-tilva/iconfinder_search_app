package com.example.assignmentkakcho.data.Repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.assignmentkakcho.api.IconfinderApi
import com.example.assignmentkakcho.data.CategoriesPagingSource
import com.example.assignmentkakcho.data.SearchPagingSource
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
            pagingSourceFactory = { SearchPagingSource(iconfinderApi, query) }
        ).liveData

    fun getCategories() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CategoriesPagingSource(iconfinderApi) }
        ).liveData

}