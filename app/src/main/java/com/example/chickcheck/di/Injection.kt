package com.example.chickcheck.di

import com.example.chickcheck.network.ApiConfig
import com.example.chickcheck.repo.DiseaseRepository


object Injection {
    fun provideRepository(): DiseaseRepository {
        val apiService = ApiConfig.getApiService()
        return DiseaseRepository.getInstance(apiService)
    }

}