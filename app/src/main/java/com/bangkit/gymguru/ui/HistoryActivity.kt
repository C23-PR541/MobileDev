package com.bangkit.gymguru.ui

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.gymguru.R
import com.bangkit.gymguru.databinding.ActivityHistoryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resViewText = intent.getStringExtra("resViewText")
        val imageBitmap = intent.getParcelableExtra<Bitmap>("imageBitmap")


    }

}