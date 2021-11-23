package com.example.gallery.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gallery.data.ImageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


@ExperimentalPagingApi
@HiltViewModel
class ImageGalleryViewModel
@Inject constructor() : ViewModel() {


    var list = MutableLiveData<PagingData<ImageModel>>()


}