package com.example.gallery.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity
data class ImageModel(
    @PrimaryKey val id: String,
    val author: String,
    val width: String,
    val height: String,
    val url: String,
    val download_url: String

) : Serializable

