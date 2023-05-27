package com.bangkit.gymguru.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.gymguru.R
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter : RecyclerView.Adapter<DateAdapter.ViewHolder>() {

    private var dates: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = dates[position]
        holder.bind(date)
    }

    override fun getItemCount(): Int {
        return dates.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(date: String) {
            dateTextView.text = date

            // Insert line break if it's Saturday
            val layoutParams = dateTextView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(0, 0, 0, 0)
            dateTextView.layoutParams = layoutParams

            if (isSaturday(date)) {
                val newLayoutParams = dateTextView.layoutParams as ViewGroup.MarginLayoutParams
                newLayoutParams.setMargins(0, 0, 0, 16.dpToPx(itemView.context)) // Adjust the margin as needed
                dateTextView.layoutParams = newLayoutParams
            }
        }

        private fun isSaturday(date: String): Boolean {
            if (date.isNotEmpty()) {
                val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
                val parsedDate = dateFormat.parse(date)
                val calendar = Calendar.getInstance()
                calendar.time = parsedDate
                return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
            }
            return false
        }
    }

    fun setDates(dates: List<String>) {
        this.dates = dates
        notifyDataSetChanged()
    }

    private fun Int.dpToPx(context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()
    }
}

