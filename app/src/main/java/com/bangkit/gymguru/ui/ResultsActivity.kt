package com.bangkit.gymguru.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.gymguru.databinding.ActivityResultsBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import java.util.*

class ResultsActivity : AppCompatActivity() {

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

    private lateinit var binding: ActivityResultsBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

}