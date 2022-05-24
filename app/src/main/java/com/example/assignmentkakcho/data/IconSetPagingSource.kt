package com.example.assignmentkakcho.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.assignmentkakcho.api.IconfinderApi
import com.example.assignmentkakcho.data.model.temp.IconsetX
import retrofit2.HttpException
import java.io.IOException


private const val UNSPLASH_STARTING_PAGE_INDEX = 1
private const val TAG = "iconsetPaging"

class IconSetPagingSource(
    private val iconfinderApi: IconfinderApi,
    private val identifier:String,
) : PagingSource<Int, IconsetX>() {
    var lastIconSetId: String? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IconsetX> {

        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            val response = iconfinderApi.getIconSets(identifier,params.loadSize,lastIconSetId)
            val  iconSets = response.iconsets

            if(iconSets.isEmpty()){
                lastIconSetId = iconSets[iconSets.lastIndex].iconset_id.toString()
                Log.d(TAG,"lastIconSetId $lastIconSetId")
            }

            LoadResult.Page(
                data = iconSets,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (iconSets.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, IconsetX>): Int? {
        return null
    }

}