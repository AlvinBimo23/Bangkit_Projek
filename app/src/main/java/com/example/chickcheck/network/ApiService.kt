package com.example.chickcheck.network

import com.example.chickcheck.response.ResponseDisease2
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/predict")
    fun getDisease(
        @Part file: MultipartBody.Part
    ): Call<ResponseDisease2>
}