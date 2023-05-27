package com.bangkit.gymguru.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.gymguru.R

class DaysOfWeekAdapter(private val daysOfWeek: List<String>) :
    RecyclerView.Adapter<DaysOfWeekAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day_of_week, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayOfWeek = daysOfWeek[position]
        holder.bind(dayOfWeek)
    }

    override fun getItemCount(): Int {
        return daysOfWeek.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayOfWeekTextView: TextView = itemView.findViewById(R.id.day_of_week_text)

        fun bind(dayOfWeek: String) {
            dayOfWeekTextView.text = dayOfWeek
        }
    }
}