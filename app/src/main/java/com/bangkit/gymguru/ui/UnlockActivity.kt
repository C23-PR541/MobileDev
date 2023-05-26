package com.bangkit.gymguru.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.gymguru.R
import com.ncorti.slidetoact.SlideToActView

class UnlockActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unlock)

        val unlock: SlideToActView = findViewById(R.id.unlock)
        unlock.animDuration = 600
        unlock.onSlideCompleteListener = object: SlideToActView.OnSlideCompleteListener{
            override fun onSlideComplete(view: SlideToActView) {
                startActivity(Intent(this@UnlockActivity, IntroActivity::class.java))
            }
        }
    }

    override fun onResume(){
        super.onResume()
        val unlock: SlideToActView = findViewById(R.id.unlock)
        unlock.resetSlider()
    }
}