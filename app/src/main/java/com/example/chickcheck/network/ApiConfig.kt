package com.example.chickcheck.network

import com.example.chickcheck.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://chicken-disease-model-f2ucnzzmua-et.a.run.app")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build()

            return retrofit.create(ApiService::class.java)
        }
    }
}