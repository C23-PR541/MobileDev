package com.bangkit.gymguru.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.gymguru.databinding.ActivityDetailCalendarBinding

class DetailCalendarActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}