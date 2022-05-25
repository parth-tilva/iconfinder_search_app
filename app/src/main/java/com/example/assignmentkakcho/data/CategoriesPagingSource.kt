package com.example.assignmentkakcho.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.assignmentkakcho.api.IconfinderApi
import com.example.assignmentkakcho.data.model.Category
import retrofit2.HttpException
import java.io.IOException


private const val UNSPLASH_STARTING_PAGE_INDEX = 1
private const val TAG = "CategoriesPaging"

class CategoriesPagingSource(
    private val iconfinderApi: IconfinderApi,
) : PagingSource<Int, Category>() {
    var lastCategory: String? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Category> {

        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            val response = iconfinderApi.getCategories(params.loadSize,lastCategory)
            val  categories = response.categories

            if(categories.isNotEmpty()){
                lastCategory = categories[categories.lastIndex].identifier
                Log.d(TAG,"last category $lastCategory")
            }

            LoadResult.Page(
                data = categories,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (categories.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Category>): Int? {
        return null
    }

}