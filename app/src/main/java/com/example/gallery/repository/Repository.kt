package com.example.gallery.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.gallery.data.ImageModel
import com.example.gallery.network.ApiService
import com.example.gallery.offlineDb.db.AppDatabase
import com.example.gallery.offlineDb.mediator.ImageMediator
import com.example.gallery.utils.AppHelper.DEFAULT_PAGE_SIZE
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
@ExperimentalPagingApi
class Repository @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) {


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