package com.bangkit.gymguru.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.gymguru.R
import com.bangkit.gymguru.ui.DetailCalendarActivity

class CalendarAdapter(private val months: List<String>, private val dates: List<List<String>>, private val dateClickListener: DateAdapter.DateClickListener) :
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

        init {
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailCalendarActivity::class.java)
                intent.putExtra("selectedMonthPosition", adapterPosition) // Pass the selected month position
                intent.putExtra("selectedDatesPosition", adapterPosition)
                context.startActivity(intent)
            }
        }

        fun bind(month: String, dates: List<String>) {
            monthTitle.text = month
            setupDaysOfWeekRecyclerView()
            setupCalendarRecyclerView(dates)
        }

        private fun setupDaysOfWeekRecyclerView() {
            val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

            val daysOfWeekAdapter = DaysOfWeekAdapter(daysOfWeek)

            daysOfWeekRecyclerView.adapter = daysOfWeekAdapter
            daysOfWeekRecyclerView.layoutManager = GridLayoutManager(itemView.context, 7)
        }

        private fun setupCalendarRecyclerView(dates: List<String>) {
            val dateAdapter = DateAdapter(dateClickListener)
            dateAdapter.setDates(dates)

            calendarRecyclerView.adapter = dateAdapter
            calendarRecyclerView.layoutManager = GridLayoutManager(itemView.context, 7)
        }
    }
}
