package com.example.gallery.offlineDb

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gallery.data.ImageModel

@Dao
interface ImageModelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(doggoModel: List<ImageModel>)

    @Query("SELECT * FROM imagemodel")
    fun getAllImageModel(): PagingSource<Int, ImageModel>

    @Query("DELETE FROM imagemodel")
    suspend fun clearAllImages()
}