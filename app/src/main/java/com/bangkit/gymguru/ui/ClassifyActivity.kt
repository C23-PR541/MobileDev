package com.bangkit.gymguru.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bangkit.gymguru.R
import com.bangkit.gymguru.databinding.ActivityClassifyBinding
import com.bangkit.gymguru.ml.ConvertedModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Float.max
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_IMAGE_CODE_PERMISSION = 100
private const val MAXIMAL_SIZE = 1000000
class ClassifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassifyBinding
    private var imageFile: File? = null
    lateinit var bitmap: Bitmap

    companion object{
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_IMAGE_PICK = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var labels = application.assets.open("labels.txt").bufferedReader().readLines()

        var imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        binding.btnClassify.setOnClickListener{
            if (::bitmap.isInitialized) {
                var tensorImage = TensorImage(DataType.UINT8)
                tensorImage.load(bitmap)

                tensorImage = imageProcessor.process(tensorImage)

                val model = ConvertedModel.newInstance(this)

                // Creates inputs for reference.
                val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
                inputFeature0.loadBuffer(tensorImage.buffer)

                // Runs model inference and gets result.
                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

                var maxIdx = 0
                outputFeature0.forEachIndexed { index, value ->
                    if (value > outputFeature0[maxIdx]) {
                        maxIdx = index
                    }
                }

                val intent = Intent(this, ResultsActivity::class.java)
                intent.putExtra("label", labels[maxIdx])
                intent.putExtra("imageFilePath", imageFile?.path)
                startActivity(intent)
                // Releases model resources if no longer used.
                model.close()
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCamera.setOnClickListener {
            checkPermission(REQUEST_IMAGE_CAPTURE)
        }

        binding.btnGallery.setOnClickListener{
            checkPermission(REQUEST_IMAGE_PICK)
        }

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_view)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_menu -> {
                    startActivity(Intent(this, CalendarActivity::class.java))
                    // Handle home menu item click
                    true
                }
                R.id.camera_menu -> {
                    // Handle camera menu item click
                    true
                }
                R.id.history_menu -> {
                    // Handle history menu item click
                    startActivity(Intent(this, CalculatorActivity::class.java))
                    true
                }
                R.id.profile_menu -> {
                    // Handle profile menu item click
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
    override fun onResume() {
        super.onResume()
        // Set the profile menu item as selected
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_view)
        bottomNavigation.menu.findItem(R.id.camera_menu).isChecked = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.ivItem.setImageBitmap(bitmap)

                    imageFile = createImageFile()
                    val outputStream = FileOutputStream(imageFile)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.close()

                    this.bitmap = bitmap

                }
                REQUEST_IMAGE_PICK -> {
                    val imageUri = data?.data
                    binding.ivItem.setImageURI(imageUri)

                    val inputStream = contentResolver.openInputStream(imageUri!!)
                    imageFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${System.currentTimeMillis()}.jpg")
                    val outputStream = FileOutputStream(imageFile)
                    val buf = ByteArray(1024)
                    var len: Int
                    if (inputStream != null) {
                        while (inputStream.read(buf).also { len = it } > 0) {
                            outputStream.write(buf, 0, len)
                        }
                    }
                    outputStream.close()
                    if (inputStream != null) {
                        inputStream.close()
                    }
                    val decodedBitmap = BitmapFactory.decodeFile(imageFile?.path)
                    this.bitmap = decodedBitmap
                }
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            imageFile = this
        }
    }

    private fun reduceFileImage(imageFile: File): File {
        val bitmap = BitmapFactory.decodeFile(imageFile.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(imageFile))
        return imageFile
    }

    private fun checkPermission(code: Int) {
        if (isGranted(
                this,
                Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_IMAGE_CODE_PERMISSION,
            )
        ) {
            if (code == REQUEST_IMAGE_PICK) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_IMAGE_PICK)
            } else {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int,
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show() // tampilin dialog bebas
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }
}