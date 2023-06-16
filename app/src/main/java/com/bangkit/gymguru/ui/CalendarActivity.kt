package com.bangkit.gymguru.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import android.window.SplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.gymguru.R
import com.bangkit.gymguru.adapter.CalendarAdapter
import com.bangkit.gymguru.adapter.DateAdapter
import com.bangkit.gymguru.databinding.ActivityCalendarBinding
import com.bangkit.gymguru.splashscreen.SplashActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity(), DateAdapter.DateClickListener {

    private lateinit var binding: ActivityCalendarBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CalendarAdapter
    private lateinit var prefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = prefs.getString("uid", null)
        val name = prefs.getString("name", null)

        if (userId == null || name == null) {
            // Data sesi belum tersimpan, kembali ke MainActivity
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        } else {

        }

        recyclerView = binding.rvCalendar
        recyclerView.layoutManager = LinearLayoutManager(this)

        val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val dates = generateDatesForYear(2023)

        adapter = CalendarAdapter(months, dates, this)
        recyclerView.adapter = adapter

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_view)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_menu -> {
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
                    startActivity(Intent(this, ProfileActivity::class.java))
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
        bottomNavigation.menu.findItem(R.id.home_menu).isChecked = true
    }

    private fun generateDatesForYear(year: Int): List<List<String>> {
        val dates = mutableListOf<List<String>>()
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())

        for (month in 0 until 12) {
            val monthDates = mutableListOf<String>()
            calendar.set(year, month, 2) // Set the first day of the month
            val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

            val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val offset = if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - 2

            for (i in 0 until offset) {
                monthDates.add("") // Add empty strings for the preceding days
            }
            for (day in 1..daysInMonth) {
                calendar.set(year, month, day)
                val date = dateFormat.format(calendar.time)
                monthDates.add(date)
            }
            dates.add(monthDates)
        }
        return dates
    }

    override fun onDateClicked(date: String) {
        val year = 2023
        Toast.makeText(this, "Clicked date: $date/$year", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        // Check if the user is logged in
        val userId = prefs.getString("uid", null)
        val name = prefs.getString("name", null)

        if (userId != null && name != null) {
            // User is logged in, close the app
            finishAffinity() // Close all activities in the task
        } else {
            // User is not logged in, perform default back button behavior
            super.onBackPressed()
        }
    }
}