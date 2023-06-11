package com.bangkit.gymguru.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.gymguru.R
import com.bangkit.gymguru.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewProfileContainer.setOnClickListener {
            startActivity(Intent(this, ViewProfileActivity::class.java))
        }

        binding.historyContainer.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.settingsContainer.setOnClickListener {
            // handle button
        }

        binding.logoutContainer.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_view)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_menu -> {
                    startActivity(Intent(this, CalendarActivity::class.java))
                    // Handle home menu item click
                    true
                }
                R.id.camera_menu -> {
                    // Handle camera menu item click
                    startActivity(Intent(this, ClassifyActivity::class.java))
                    true
                }
                R.id.history_menu -> {
                    // Handle history menu item click
                    startActivity(Intent(this, CalculatorActivity::class.java))
                    true
                }
                R.id.profile_menu -> {
                    // Handle profile menu item click
                    true
                }

                else -> false
            }
        }
    }
    override fun onResume() {
        super.onResume()
        // Set the profile menu item as selected
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_view)
        bottomNavigation.menu.findItem(R.id.profile_menu).isChecked = true
    }

}