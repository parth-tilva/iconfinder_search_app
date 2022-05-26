package com.example.assignmentkakcho.ui.category

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.assignmentkakcho.data.repository.IconfinderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "categoryViewModel"

@HiltViewModel
class CategoryViewModel @Inject constructor(private val iconRepo: IconfinderRepository) :
    ViewModel() {

    val categoryList = iconRepo.getCategories().cachedIn(viewModelScope)

    private val currentCategory : MutableLiveData<String> = MutableLiveData()
    val iconSetList  = currentCategory.switchMap {
        iconRepo.getIconSets(it).cachedIn(viewModelScope)
    }

    fun getIconSets(query: String){
        if(query != currentCategory.value)
        currentCategory.value = query
    }
}
