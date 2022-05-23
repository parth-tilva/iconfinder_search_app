package com.example.assignmentkakcho.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.assignmentkakcho.api.IconfinderApi
import com.example.assignmentkakcho.data.model.Icon
import com.example.assignmentkakcho.data.model.temp.IconSet
import com.example.assignmentkakcho.data.model.temp.IconsetX
import retrofit2.HttpException
import java.io.IOException


private const val UNSPLASH_STARTING_PAGE_INDEX = 1
private const val NETWORK_PAGE_SIZE = 25

class IconPagingSource(
    private val iconfinderApi: IconfinderApi,
    private val iconSet: List<IconsetX>
) : PagingSource<Int, Icon>() {

    var setPos = 0;
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Icon> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        val offset = if (params.key != null) ((position - 1) * NETWORK_PAGE_SIZE) else 0 // 1

        return try {
            val response = iconfinderApi.getIconFromIconSet(iconSet[setPos].iconset_id, NETWORK_PAGE_SIZE, offset)
            val photos = response.icons

            val nextKey = if (photos.isEmpty()) {
                setPos++
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