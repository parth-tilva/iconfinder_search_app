package com.example.assignmentkakcho.ui.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.assignmentkakcho.data.Repository.IconfinderRepository
import com.example.assignmentkakcho.data.model.temp.IconSet
import com.example.assignmentkakcho.data.model.temp.IconsetX
import com.example.assignmentkakcho.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(private val iconRepo: IconfinderRepository): ViewModel() {

    val TAG = "categoryviewmodel"
    val categoryList = iconRepo.getCategories()
    lateinit var  iconSet: List<IconsetX>

//    private val _iconSetList = MutableLiveData<PagingData<IconsetX>>()
//    val iconSetList = _iconSetList



    fun getIconSets(category: String): LiveData<PagingData<IconsetX>> {
        return iconRepo.getIconSets(category)
    }




//    fun getIconSets(identifier: String){
//        viewModelScope.launch {
//            val resource = iconRepo.getIconSets(identifier)
//            when(resource){
//                is Resource.Success ->{
//                    Log.d(TAG,"resource successful")
//                    iconSet = resource.data!!.iconsets
//                }
//                is Resource.Failure ->{
//
//                }
//            }
//        }
//    }
}
