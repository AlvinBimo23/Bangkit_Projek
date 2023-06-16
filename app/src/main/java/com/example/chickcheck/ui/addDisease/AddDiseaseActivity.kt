package com.example.chickcheck.ui.addDisease

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.chickcheck.R
import com.example.chickcheck.utils.ViewModelFactory
import com.example.chickcheck.camera.CameraActivity
import com.example.chickcheck.databinding.ActivityAddDiseaseBinding
import com.example.chickcheck.response.ResponseDisease2
import com.example.chickcheck.utils.reduceFileImage
import com.example.chickcheck.utils.rotatePicture
import com.example.chickcheck.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import com.example.chickcheck.utils.Result


class AddDiseaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDiseaseBinding
    private var getFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDiseaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        val factory: ViewModelFactory = ViewModelFactory.getInstance()
        val viewModel: AddDiseaseViewModel by viewModels { factory }

        binding.btnCamera.setOnClickListener { goCameraX() }
        binding.btnGallery.setOnClickListener { goGallery() }
        binding.btnUpload.setOnClickListener {
            if (getFile != null) {
                val file = reduceFileImage(getFile as File)
                val requestImage = file.asRequestBody("image/jpeg".toMediaType())
                val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "file",
                    file.name,
                    requestImage
                )
                viewModel.uploadDiseaseUser(imageMultiPart).observe(this){ result ->
                    when(result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.INVISIBLE
                            val error = result.error
                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.INVISIBLE
                            val data = result.data

                            fun showSecondDialog(data: ResponseDisease2) {
                                val dialog2 = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                dialog2.apply {
                                    titleText = "Penanganan"
                                    contentText = data.penanganan
                                    setConfirmClickListener {
                                        it.dismiss()
                                    }
                                    dialog2.show()
                                }
                            }

                            fun showThreeDialog(data: ResponseDisease2) {
                                val dialog3 = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                dialog3.apply {
                                    titleText = "Gejala"
                                    contentText = data.gejala
                                    setConfirmClickListener {
                                        it.dismiss()
                                    }
                                    dialog3.show()
                                }
                            }

                            val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            dialog.apply {
                                titleText = data.penyakit
                                contentText = data.deskripsi
                                setConfirmClickListener {
                                    it.dismiss()
                                    showSecondDialog(data)
                                    showThreeDialog(data)
                                }
                                    show()
                            }

                        }
                    }
                }
            } else {
                Toast.makeText(this@AddDiseaseActivity, R.string.photReqInput, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, R.string.permissionCam, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private val launcherIntentCameraX =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CAMERA_X_RESULT) {
                val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getSerializableExtra("picture", File::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    it.data?.getSerializableExtra("picture")
                } as? File
                val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
                myFile?.let { filePicture ->
                    rotatePicture(filePicture, isBackCamera)
                    getFile = filePicture
                    binding.ivDisease.setImageBitmap(BitmapFactory.decodeFile(filePicture.path))
                }
            }

        }

    private fun goCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun goGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val item = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(item)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddDiseaseActivity)
                getFile = myFile
                binding.ivDisease.setImageURI(uri)
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}