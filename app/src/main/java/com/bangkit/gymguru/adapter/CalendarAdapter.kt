package com.bangkit.gymguru.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.gymguru.R


class CalendarAdapter(private val months: List<String>, private val dates: List<List<String>>) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val month = months[position]
        val monthDates = dates[position]
        holder.bind(month, monthDates)
    }

    override fun getItemCount(): Int {
        return months.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val monthTitle: TextView = itemView.findViewById(R.id.month_title)
        private val daysOfWeekRecyclerView: RecyclerView = itemView.findViewById(R.id.days_of_week_recycler_view)
        private val calendarRecyclerView: RecyclerView = itemView.findViewById(R.id.calendarRecyclerView)

        fun bind(month: String, dates: List<String>) {
            monthTitle.text = month
            setupDaysOfWeekRecyclerView()
            setupCalendarRecyclerView(dates)
        }

        private fun setupDaysOfWeekRecyclerView() {
            val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

            // Create a new adapter for the days of the week
            val daysOfWeekAdapter = DaysOfWeekAdapter(daysOfWeek)

            // Set the adapter to the days of the week RecyclerView
            daysOfWeekRecyclerView.adapter = daysOfWeekAdapter
            daysOfWeekRecyclerView.layoutManager = GridLayoutManager(itemView.context, 7)
        }

        private fun setupCalendarRecyclerView(dates: List<String>) {
            // Create a new adapter for the dates
            val dateAdapter = DateAdapter()
            dateAdapter.setDates(dates)

            // Set the adapter to the calendar RecyclerView
            calendarRecyclerView.adapter = dateAdapter
            calendarRecyclerView.layoutManager = GridLayoutManager(itemView.context, 7)
        }
    }
}