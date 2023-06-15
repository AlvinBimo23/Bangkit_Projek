package com.example.chickcheck.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.chickcheck.R
import com.example.chickcheck.ui.addDisease.AddDiseaseActivity

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val splashScreenDuration = 3000L // Waktu tampilan Splash Screen (dalam milidetik)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            // Setelah waktu tampilan Splash Screen selesai, pindah ke aktivitas berikutnya
            val intent = Intent(this, AddDiseaseActivity::class.java)
            startActivity(intent)
            finish()
        }, splashScreenDuration)
    }
}