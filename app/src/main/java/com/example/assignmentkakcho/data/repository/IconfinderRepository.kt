package com.example.assignmentkakcho.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.assignmentkakcho.api.IconfinderApi
import com.example.assignmentkakcho.data.CategoriesPagingSource
import com.example.assignmentkakcho.data.IconPagingSource
import com.example.assignmentkakcho.data.IconSetPagingSource
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
                pageSize = 15,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CategoriesPagingSource(iconfinderApi) }
        ).liveData

    fun getIconSets(category: String) =
        Pager(
            config = PagingConfig(
                pageSize = 12,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { IconSetPagingSource(iconfinderApi, category) }
        ).liveData


    fun getIconsResults(iconSetId: Int) =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { IconPagingSource(iconfinderApi, iconSetId) }
        ).liveData

}