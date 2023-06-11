package com.bangkit.gymguru.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.gymguru.databinding.ActivityGraphicBinding

class GraphicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGraphicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphicBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}