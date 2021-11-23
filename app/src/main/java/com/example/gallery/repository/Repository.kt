package com.example.gallery.repository

import androidx.paging.ExperimentalPagingApi
import com.example.gallery.data.ImageModel
import com.example.gallery.network.ApiService

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
@ExperimentalPagingApi
class Repository @Inject constructor(val apiService: ApiService) {
    fun getImages(i: Int): Flow<List<ImageModel>> {
        return flow {
            val imageList = apiService.getImages(i,100)

            emit(imageList)
        }.flowOn(Dispatchers.IO)
    }

}