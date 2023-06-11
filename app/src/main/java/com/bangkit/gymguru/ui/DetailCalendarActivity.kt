package com.bangkit.gymguru.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.gymguru.R
import com.bangkit.gymguru.adapter.DateAdapter
import com.bangkit.gymguru.adapter.DaysOfWeekAdapter
import com.bangkit.gymguru.databinding.ActivityDetailCalendarBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class DetailCalendarActivity : AppCompatActivity(), DateAdapter.DateClickListener {

    private lateinit var binding: ActivityDetailCalendarBinding
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var dateAdapter: DateAdapter
    private val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendarRecyclerView = binding.calendarRecyclerView
        calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)

        val selectedMonthPosition = intent.getIntExtra("selectedMonthPosition", 0)
        val selectedMonth = months[selectedMonthPosition]
        val year = 2023

        // Retrieve the dates for the selected month and year
        val dates = generateDatesForMonth(year, selectedMonthPosition)

        dateAdapter = DateAdapter(this)
        dateAdapter.setDates(dates)

        binding.monthTitle.text = "$selectedMonth"

        binding.daysOfWeekRecyclerView.layoutManager = GridLayoutManager(this, 7)
        binding.daysOfWeekRecyclerView.adapter = DaysOfWeekAdapter(listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"))

        calendarRecyclerView.adapter = dateAdapter

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_view)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_menu -> {
                    // Handle home menu item click
                    startActivity(Intent(this, CalendarActivity::class.java))
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
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_view)
        bottomNavigation.menu.findItem(R.id.home_menu).isEnabled = true
    }

    private fun generateDatesForMonth(year: Int, month: Int): List<String> {
        val dates = mutableListOf<String>()
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 2)
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // Determine the offset to adjust the starting position of dates
        val offset = if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - 2

        // Add empty strings for the preceding days
        for (i in 0 until offset) {
            dates.add("")
        }

        for (day in 1..daysInMonth) {
            calendar.set(year, month, day)
            val date = dateFormat.format(calendar.time)
            dates.add(date)
        }
        return dates
    }

    override fun onDateClicked(date: String) {
        Toast.makeText(this, "Clicked date: $date", Toast.LENGTH_SHORT).show()
    }
}