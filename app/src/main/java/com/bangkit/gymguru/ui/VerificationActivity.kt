package com.bangkit.gymguru.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.gymguru.databinding.ActivityVerificationBinding

class VerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}