package com.example.assignmentkakcho.ui.gallery

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.assignmentkakcho.data.Repository.IconfinderRepository
import com.example.assignmentkakcho.data.model.Icon
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: IconfinderRepository,
) : ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    var currentIcon:Icon? = null

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val DEFAULT_QUERY = "Social Media"
    }
}