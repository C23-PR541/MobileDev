package com.bangkit.gymguru.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.gymguru.databinding.ActivityVerificationTwoBinding

class VerificationTwoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityVerificationTwoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}