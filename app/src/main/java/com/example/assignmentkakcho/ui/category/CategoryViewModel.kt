package com.example.assignmentkakcho.ui.category

import androidx.lifecycle.ViewModel
import com.example.assignmentkakcho.data.Repository.IconfinderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(private val iconRepo: IconfinderRepository): ViewModel() {

    val categoryList = iconRepo.getCategories()

}
