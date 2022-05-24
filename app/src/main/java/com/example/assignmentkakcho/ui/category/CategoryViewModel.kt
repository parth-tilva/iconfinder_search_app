package com.example.assignmentkakcho.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.assignmentkakcho.data.repository.IconfinderRepository
import com.example.assignmentkakcho.data.model.IconSet
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(private val iconRepo: IconfinderRepository): ViewModel() {

    val TAG = "categoryviewmodel"
    val categoryList = iconRepo.getCategories()
    lateinit var  iconSet: List<IconSet>

//    private val _iconSetList = MutableLiveData<PagingData<IconSet>>()
//    val iconSetList = _iconSetList



    fun getIconSets(category: String): LiveData<PagingData<IconSet>> {
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
