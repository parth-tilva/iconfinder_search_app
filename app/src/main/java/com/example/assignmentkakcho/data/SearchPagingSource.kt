package com.example.assignmentkakcho.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.assignmentkakcho.api.IconfinderApi
import com.example.assignmentkakcho.data.model.Icon
import retrofit2.HttpException
import java.io.IOException


private const val UNSPLASH_STARTING_PAGE_INDEX = 1
private const val NETWORK_PAGE_SIZE = 25
private const val TAG ="searchPaging"


class SearchPagingSource(
    private val iconfinderApi: IconfinderApi,
    private val query: String
) : PagingSource<Int, Icon>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Icon> {
        Log.d(TAG,"loading called")

        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        val offset = if (params.key != null) ((position - 1) * NETWORK_PAGE_SIZE) else 0 // 1

        return try {
            val response = iconfinderApi.searchPhotos(query, NETWORK_PAGE_SIZE, offset)
            val photos = response.icons

            Log.d(TAG,"loading search photo$query")
            Log.d(TAG,"phots$photos")
            val nextKey = if (photos.isEmpty()) {
                null
            } else {
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, Icon>): Int? {
        return null
    }
}