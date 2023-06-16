package com.bangkit.gymguru.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bangkit.gymguru.data.ResultData
import com.bangkit.gymguru.databinding.ActivityResultsBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultsBinding
    private lateinit var database: FirebaseDatabase

    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        val label = intent.getStringExtra("label")
        val imageFilePath = intent.getStringExtra("imageFilePath")
        val bitmap = BitmapFactory.decodeFile(imageFilePath)

        val resView = binding.tvToolsName
        val imageView = binding.ivResult

        resView.text = label
        imageView.setImageBitmap(bitmap)

        if (label == "Aerobic Stepper"){
            val videoUrls = listOf(AEROBIC_ONE, AEROBIC_TWO, AEROBIC_THREE)
            val random = Random()
            val randomUrl = videoUrls[random.nextInt(videoUrls.size)]

            val player = ExoPlayer.Builder(this).build()
            binding.videoView.player = player
            val mediaItem = MediaItem.fromUri(randomUrl)
            player.setMediaItem(mediaItem)
            player.prepare()

        } else if (label == "Dumbbell"){
            val videoUrls = listOf(DUMBBELL_ONE, DUMBBELL_TWO, DUMBBELL_THREE)
            val random = Random()
            val randomUrl = videoUrls[random.nextInt(videoUrls.size)]

            val player = ExoPlayer.Builder(this).build()
            binding.videoView.player = player
            val mediaItem = MediaItem.fromUri(randomUrl)
            player.setMediaItem(mediaItem)
            player.prepare()

        } else if (label == "Treadmill"){
            val videoUrls = listOf(TREADMILL_ONE, TREADMILL_TWO, TREADMILL_THREE)
            val random = Random()
            val randomUrl = videoUrls[random.nextInt(videoUrls.size)]

            val player = ExoPlayer.Builder(this).build()
            binding.videoView.player = player
            val mediaItem = MediaItem.fromUri(randomUrl)
            player.setMediaItem(mediaItem)
            player.prepare()
        }

        binding.btnSave.setOnClickListener {
            // Call a function to upload the data to Firebase Storage
            uploadDataToStorage()
            startActivity(Intent(this, HistoryActivity::class.java))
            finish()
        }
    }

    private fun uploadDataToStorage() {
        val resViewText = binding.tvToolsName.text.toString()
        val imageViewBitmap = (binding.ivResult.drawable as BitmapDrawable).bitmap
        val playerUrl = getPlayerUrl()

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserId != null) {
            val resultsRef = database.getReference("results").child(currentUserId)

            val resultKey = resultsRef.push().key

            if (resultKey != null) {
                // Get the current date and time
                val currentDateTime = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Date())

                // Create a data object with the necessary information including the current date and time
                val resultData = playerUrl?.let { ResultData(resViewText, "", it, currentDateTime) }

                val imageFileName = UUID.randomUUID().toString()
                val imageRef = storageReference.child("images/$imageFileName.jpg")

                val baos = ByteArrayOutputStream()
                imageViewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val imageData = baos.toByteArray()

                val uploadTask = imageRef.putBytes(imageData)

                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    imageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val imageUrl = task.result.toString()

                        if (resultData != null) {
                            resultData.imageUrl = imageUrl
                        }

                        resultsRef.child(resultKey).setValue(resultData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "Failed to save data: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getPlayerUrl(): String? {
        val currentMediaItem = binding.videoView.player?.currentMediaItem
        return currentMediaItem?.playbackProperties?.uri?.toString()
    }

    override fun onPause() {
        super.onPause()
        binding.videoView.player?.pause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        binding.videoView.player?.stop()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }

    companion object {
        const val AEROBIC_ONE = "https://github.com/C23-PR541/MobileDev/raw/main/Videos/Aerobic%20Stepper%201.mp4"
        const val AEROBIC_TWO = "https://github.com/C23-PR541/MobileDev/raw/main/Videos/Aerobic%20Stepper%202.mp4"
        const val AEROBIC_THREE = "https://github.com/C23-PR541/MobileDev/raw/main/Videos/Aerboic%20Stepper%203.mp4"

        const val DUMBBELL_ONE = "https://github.com/C23-PR541/MobileDev/raw/main/Videos/Dumbbell%201.mp4"
        const val DUMBBELL_TWO = "https://github.com/C23-PR541/MobileDev/raw/main/Videos/Dumbell%202.mp4"
        const val DUMBBELL_THREE = "https://github.com/C23-PR541/MobileDev/raw/main/Videos/Dumbell%203.mp4"

        const val TREADMILL_ONE = "https://github.com/C23-PR541/MobileDev/raw/main/Videos/Treadmill%201.mp4"
        const val TREADMILL_TWO = "https://github.com/C23-PR541/MobileDev/raw/main/Videos/Treadmill%202.mp4"
        const val TREADMILL_THREE = "https://github.com/C23-PR541/MobileDev/raw/main/Videos/Treadmill%203.mp4"
    }
}