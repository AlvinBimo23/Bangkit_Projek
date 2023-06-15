package com.example.chickcheck.ui.addDisease

import androidx.lifecycle.ViewModel
import com.example.chickcheck.repo.DiseaseRepository
import okhttp3.MultipartBody

class AddDiseaseViewModel(private val diseaseRepository: DiseaseRepository) : ViewModel() {
    fun uploadDiseaseUser(
        photo: MultipartBody.Part
    ) = diseaseRepository.postingNewDiseaseUser(photo)
}