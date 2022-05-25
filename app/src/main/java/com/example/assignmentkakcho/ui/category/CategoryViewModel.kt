package com.example.assignmentkakcho.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.assignmentkakcho.data.repository.IconfinderRepository
import com.example.assignmentkakcho.data.model.IconSet
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "categoryViewModel"

@HiltViewModel
class CategoryViewModel @Inject constructor(private val iconRepo: IconfinderRepository): ViewModel() {

    val categoryList = iconRepo.getCategories()
    lateinit var  iconSet: List<IconSet>

    fun getIconSets(category: String): LiveData<PagingData<IconSet>> {
        return iconRepo.getIconSets(category)
    }

}
