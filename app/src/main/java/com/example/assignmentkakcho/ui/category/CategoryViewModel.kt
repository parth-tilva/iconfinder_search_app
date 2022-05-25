package com.example.assignmentkakcho.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

     lateinit var iconSetList :LiveData<PagingData<IconSet>>

    fun getIconSets(category: String) {
        iconSetList  = iconRepo.getIconSets(category)
        return
    }
}
