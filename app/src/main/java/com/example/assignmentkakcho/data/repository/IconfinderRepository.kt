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
                pageSize = 20,
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
            pagingSourceFactory = { IconSetPagingSource(iconfinderApi,category) }
        ).liveData


//    suspend fun getIconSets(identifier: String):Resource<IconSetResponse>{
//        return try{
//            val response = iconfinderApi.getIconSets(identifier,100,null)
//            val result = response.body()
//            if(response.isSuccessful && result!=null){
//                Resource.Success(result)
//            }else{
//                Resource.Failure(response.message() ?: "response null",null)
//            }
//
//        } catch (e: IOException){
//            Resource.Failure(e.message?: " an error occurred",null)
//        } catch (e: HttpException){
//            Resource.Failure(e.message?: " an error occurred",null)
//        } catch (e: Exception) {
//            Resource.Failure(e.message ?: " an error occurred", null)
//        }
//    }

    fun getIconsResults(iconSetId: Int) =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { IconPagingSource(iconfinderApi, iconSetId) }
        ).liveData

}