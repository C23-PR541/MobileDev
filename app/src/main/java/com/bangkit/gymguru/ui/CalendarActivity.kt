package com.bangkit.gymguru.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.gymguru.R
import com.bangkit.gymguru.adapter.CalendarAdapter
import com.bangkit.gymguru.adapter.DateAdapter
import com.bangkit.gymguru.databinding.ActivityCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity(), DateAdapter.DateClickListener {

    private lateinit var binding: ActivityCalendarBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CalendarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.rvCalendar
        recyclerView.layoutManager = LinearLayoutManager(this)

        val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val dates = generateDatesForYear(2023)

        adapter = CalendarAdapter(months, dates, this)
        recyclerView.adapter = adapter
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
}