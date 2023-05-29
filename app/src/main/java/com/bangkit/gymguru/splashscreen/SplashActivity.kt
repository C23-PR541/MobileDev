package com.bangkit.gymguru.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.gymguru.databinding.ActivitySplashBinding
import com.bangkit.gymguru.ui.UnlockActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val SPLASH_TIME_OUT = 1000L // Splash screen display time in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Delay the start of the main activity
        val intent = Intent(this, UnlockActivity::class.java)
        val splashTimer = object : Thread() {
            override fun run() {
                try {
                    sleep(SPLASH_TIME_OUT)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    startActivity(intent)
                    finish()
                }
            }
        }
        splashTimer.start()
    }
}