package com.example.chickcheck.repo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.chickcheck.network.ApiService
import com.example.chickcheck.response.ResponseDisease2
import com.example.chickcheck.utils.Result
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiseaseRepository private constructor(private val apiService: ApiService ){

    private val postDiseaseNew = MediatorLiveData<Result<ResponseDisease2>>()

    fun postingNewDiseaseUser(
        photo: MultipartBody.Part
    ): LiveData<Result<ResponseDisease2>> {
        postDiseaseNew.value = Result.Loading
        apiService.getDisease(
            photo
        ).enqueue(object : Callback<ResponseDisease2>{
            override fun onResponse(
                call: Call<ResponseDisease2>,
                response: Response<ResponseDisease2>
            ) {
                if (response.isSuccessful) {
                    val request = response.body()
                    postDiseaseNew.value = Result.Success(request!!)
                    Log.d("Testing", request.toString())
                } else {
                    postDiseaseNew.value = Result.Error("Failed load data")
                }
            }

            override fun onFailure(call: Call<ResponseDisease2>, t: Throwable) {
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