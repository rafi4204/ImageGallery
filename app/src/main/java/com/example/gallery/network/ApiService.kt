package com.example.gallery.network

import com.example.gallery.data.ImageModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/list")
    suspend fun getImages(@Query("page") page: Int, @Query("limit") limit: Int): List<ImageModel>
}