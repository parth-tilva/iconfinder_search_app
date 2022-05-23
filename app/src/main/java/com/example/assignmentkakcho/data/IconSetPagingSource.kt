package com.example.assignmentkakcho.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.assignmentkakcho.api.IconfinderApi
import com.example.assignmentkakcho.data.model.temp.IconsetX
import java.io.IOException


private const val UNSPLASH_STARTING_PAGE_INDEX = 1
private const val NETWORK_PAGE_SIZE = 25
private const val TAG = "iconsetPaging"

class IconSetPagingSource(
    private val iconfinderApi: IconfinderApi,
    private val identifier:String,
) : PagingSource<Int, IconsetX>() {
    var lastIconSet: String? = null

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, IconsetX> {

        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            val response = iconfinderApi.getIconSets(identifier,params.loadSize,lastIconSet)
            val  iconSets = response.body()?.iconsets

            if(iconSets!=null){
                lastIconSet = iconSets[ic]   [categories.lastIndex].identifier
                Log.d(TAG,"lastIconSet $lastIconSet")
            }

            PagingSource.LoadResult.Page(
                data = categories,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (categories.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            PagingSource.LoadResult.Error(exception)
        } catch (exception: HttpException) {
            PagingSource.LoadResult.Error(exception)
        } catch (e: Exception) {
            PagingSource.LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Category>): Int? {
        return null
    }

}