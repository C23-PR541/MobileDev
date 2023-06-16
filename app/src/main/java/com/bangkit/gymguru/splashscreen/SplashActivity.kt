package com.bangkit.gymguru.splashscreen

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.view.isVisible
import com.bangkit.gymguru.R
import com.bangkit.gymguru.databinding.ActivitySplashBinding
import com.bangkit.gymguru.ui.UnlockActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val SPLASH_TIME_OUT = 1000L // Splash screen display time in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Delay the start of the UnlockActivity
        val handler = Handler()
        handler.postDelayed({
            // Start the UnlockActivity after the specified SPLASH_TIME_OUT
            val intent = Intent(this@SplashActivity, UnlockActivity::class.java)
//            val options = ActivityOptions.makeSceneTransitionAnimation(this@SplashActivity).toBundle()
            startActivity(intent /*, options*/)
            finish()
        }, SPLASH_TIME_OUT)
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

    override fun onResume() {
        super.onResume()
        // Make sure the splash screen is visible when the activity resumes
        binding.root.isVisible = true
    }
}