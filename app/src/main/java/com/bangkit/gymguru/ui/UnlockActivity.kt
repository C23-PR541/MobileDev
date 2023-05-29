package com.bangkit.gymguru.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.gymguru.R
import com.bangkit.gymguru.databinding.ActivityUnlockBinding
import com.ncorti.slidetoact.SlideToActView

class UnlockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUnlockBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnlockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val unlock: SlideToActView = binding.unlock
        unlock.animDuration = 600
        unlock.onSlideCompleteListener = object: SlideToActView.OnSlideCompleteListener{
            override fun onSlideComplete(view: SlideToActView) {
                startActivity(Intent(this@UnlockActivity, IntroActivity::class.java))
            }
        }
    }

    override fun onResume(){
        super.onResume()
        val unlock: SlideToActView = binding.unlock
        unlock.resetSlider()
    }
}