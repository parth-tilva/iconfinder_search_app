package com.example.assignmentkakcho.ui.category

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.assignmentkakcho.data.repository.IconfinderRepository
import com.example.assignmentkakcho.data.model.IconSet
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "categoryViewModel"

@HiltViewModel
class CategoryViewModel @Inject constructor(private val iconRepo: IconfinderRepository) :
    ViewModel() {

    val categoryList = iconRepo.getCategories().cachedIn(viewModelScope)
    lateinit var iconSet: List<IconSet>


    private val currentCategory = MutableLiveData("")

    val iconSetList  = currentCategory.switchMap {
        iconRepo.getIconSets(it).cachedIn(viewModelScope)
    }

    fun getIconSets(query: String){
        if(query != currentCategory.value)
        currentCategory.value = query
    }


}
