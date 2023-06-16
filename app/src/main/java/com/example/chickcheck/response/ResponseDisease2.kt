package com.example.chickcheck.response

import com.google.gson.annotations.SerializedName

data class ResponseDisease2(

	@field:SerializedName("Gejala")
	val gejala: String,

	@field:SerializedName("Penanganan")
	val penanganan: String,

	@field:SerializedName("Deskripsi")
	val deskripsi: String,

	@field:SerializedName("Penyakit")
	val penyakit: String
)
