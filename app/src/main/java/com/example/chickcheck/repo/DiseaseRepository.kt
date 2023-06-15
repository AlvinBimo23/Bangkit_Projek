package com.example.chickcheck.repo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.chickcheck.network.ApiService
import com.example.chickcheck.utils.Result
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiseaseRepository private constructor(private val apiService: ApiService ){

    private val postDiseaseNew = MediatorLiveData<Result<String>>()

    fun postingNewDiseaseUser(
        photo: MultipartBody.Part
    ): LiveData<Result<String>> {
        postDiseaseNew.value = Result.Loading
        apiService.getDisease(
            photo
        ).enqueue(object : Callback<Any>{
            override fun onResponse(
                call: Call<Any>,
                response: Response<Any>
            ) {
                if (response.isSuccessful) {
                    val request = response.body().toString()
                    postDiseaseNew.value = Result.Success(request)
                    Log.d("Testing", request)
                } else {
                    postDiseaseNew.value = Result.Error("Failed load data")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                postDiseaseNew.value = Result.Error("Failed load data")
                Log.e(ContentValues.TAG, "Failed: Response Unsuccessful - ${t.message.toString()}")
            }
        })
        return postDiseaseNew
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: DiseaseRepository? = null

        fun getInstance(apiService: ApiService) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DiseaseRepository(apiService)
            }.also { INSTANCE = it }
    }

}