package com.example.gallery.offlineDb.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gallery.data.ImageModel
import com.example.gallery.offlineDb.dao.ImageModelDao
import com.example.gallery.offlineDb.RemoteKeys
import com.example.imagegallery.dao.RemoteKeysDao
import dagger.hilt.android.scopes.ActivityRetainedScoped

@ActivityRetainedScoped
@Database(version = 1, entities = [ImageModel::class, RemoteKeys::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getRepoDao(): RemoteKeysDao
    abstract fun getImageModelDao(): ImageModelDao

    companion object {

        val IMAGE_DB = "image.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, IMAGE_DB)
                .build()
    }

}