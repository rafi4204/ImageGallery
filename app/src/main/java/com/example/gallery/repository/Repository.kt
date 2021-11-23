package com.example.gallery.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.gallery.data.ImageModel
import com.example.gallery.network.ApiService
import com.example.gallery.offlineDb.DoggoImagePagingSource
import com.example.gallery.utils.AppHelper.DEFAULT_PAGE_SIZE
import com.example.gallery.offlineDb.db.AppDatabase
import com.example.gallery.offlineDb.mediator.ImageMediator

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
@ActivityRetainedScoped
@ExperimentalPagingApi
class Repository @Inject constructor(private val apiService: ApiService, private val appDatabase: AppDatabase) {

    fun getImages(i: Int): Flow<List<ImageModel>> {
        return flow {
            val imageList = apiService.getImages(i,100)

            emit(imageList)
        }.flowOn(Dispatchers.IO)
    }


    fun getImages(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<ImageModel>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { DoggoImagePagingSource(apiService) }
        ).flow
    }
    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }

    fun letImagesFlowDb(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<ImageModel>> {
        if (appDatabase == null) throw IllegalStateException("Database is not initialized")

        val pagingSourceFactory = { appDatabase.getImageModelDao().getAllImageModel() }
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = ImageMediator(apiService, appDatabase)
        ).flow
    }
}