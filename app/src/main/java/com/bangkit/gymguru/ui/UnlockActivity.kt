package com.bangkit.gymguru.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.WindowInsets
import android.view.WindowManager
import com.bangkit.gymguru.R
import com.bangkit.gymguru.databinding.ActivityUnlockBinding
import com.ncorti.slidetoact.SlideToActView

class UnlockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUnlockBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnlockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.sharedElementEnterTransition = TransitionInflater.from(this)
            .inflateTransition(android.R.transition.move)

        val unlock: SlideToActView = binding.unlock
        unlock.animDuration = 600
        unlock.onSlideCompleteListener = object: SlideToActView.OnSlideCompleteListener{
            override fun onSlideComplete(view: SlideToActView) {
                startActivity(Intent(this@UnlockActivity, IntroActivity::class.java))
            }
        }

        setupView()
    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,

                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onResume(){
        super.onResume()
        val unlock: SlideToActView = binding.unlock
        unlock.resetSlider()
    }
}