package com.bangkit.gymguru.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.gymguru.R
import com.bangkit.gymguru.databinding.ItemDayOfWeekBinding

class DaysOfWeekAdapter(private val daysOfWeek: List<String>) :
    RecyclerView.Adapter<DaysOfWeekAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDayOfWeekBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayOfWeek = daysOfWeek[position]
        holder.bind(dayOfWeek)
    }

    override fun getItemCount(): Int {
        return daysOfWeek.size
    }

    inner class ViewHolder(private val binding: ItemDayOfWeekBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dayOfWeek: String) {
            binding.dayOfWeekText.text = dayOfWeek
        }
    }
}