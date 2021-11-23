package com.example.gallery.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.gallery.data.ImageModel
import com.example.gallery.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class ImageGalleryViewModel
@Inject constructor(private val repository: Repository) : ViewModel() {


    var list = MutableLiveData<List<ImageModel>>()

    suspend fun getImage() {
        repository.getImages(1)
            .collect {
                list.value = it
            }
    }

}