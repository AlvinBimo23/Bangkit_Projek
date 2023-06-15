package com.example.chickcheck.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chickcheck.repo.DiseaseRepository
import com.example.chickcheck.ui.addDisease.AddDiseaseViewModel
import com.example.chickcheck.di.Injection

class ViewModelFactory private constructor(private val diseaseRepository: DiseaseRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddDiseaseViewModel::class.java) -> {
                AddDiseaseViewModel(diseaseRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ViewModelFactory(Injection.provideRepository())
        }.also { INSTANCE = it }
    }

}