package com.bangkit.gymguru.ui

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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

class ClassifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassifyBinding
    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var labels = application.assets.open("labels.txt").bufferedReader().readLines()

        var imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))

            .build()

        binding.btnClassify.setOnClickListener{

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

            // Convert the bitmap to a byte array
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            intent.putExtra("image", byteArray)
            startActivity(intent)
            // Releases model resources if no longer used.
            model.close()
        }

        binding.btnGallery.setOnClickListener{
            var intent: Intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 100)
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
        if (requestCode == 100) {
            var uri = data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            binding.ivItem.setImageBitmap(bitmap)
        }
    }
}