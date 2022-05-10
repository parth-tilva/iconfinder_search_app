package com.example.assignmentkakcho.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.assignmentkakcho.api.IconfinderApi
import retrofit2.HttpException
import java.io.IOException


private const val UNSPLASH_STARTING_PAGE_INDEX = 1
const val NETWORK_PAGE_SIZE = 25

class PagingSource(
    private val iconfinderApi: IconfinderApi,
    private val query: String
) : PagingSource<Int, Icon>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Icon> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        val offset = if (params.key != null) ((position - 1) * NETWORK_PAGE_SIZE)  else 0 // 1

        return try {
            val response = iconfinderApi.searchPhotos(query, NETWORK_PAGE_SIZE, offset)
            Log.d("sapi"," offset:$offset  paramsloadsize: {${params.loadSize}} switch api  ${response.toString()}, " )
            val photos = response.icons

            val nextKey = if (photos.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                Log.d("sapi"," position: $position +  next key ration ${(params.loadSize / NETWORK_PAGE_SIZE)}" )
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }

            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            Log.d("sapi","switch api $exception")
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.d("sapi","switch api $exception")
            LoadResult.Error(exception)
        } catch (e: Exception){
            Log.d("sapi","switch api $e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Icon>): Int? {
        TODO("Not yet implemented")
    }
}