package com.bangkit.gymguru.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.gymguru.R
import com.bangkit.gymguru.databinding.ItemDateBinding
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter(private val dateClickListener: DateClickListener) : RecyclerView.Adapter<DateAdapter.ViewHolder>() {

    private var dates: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = dates[position]
        holder.bind(date)
    }

    override fun getItemCount(): Int {
        return dates.size
    }

    interface DateClickListener {
        fun onDateClicked(date: String)
    }

    inner class ViewHolder(private val binding: ItemDateBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedDate = dates[position]
                    dateClickListener.onDateClicked(clickedDate)
                }
            }
        }
        fun bind(date: String) {
            binding.apply {
                binding.dateTextView.text = date

                val layoutParams = binding.dateTextView.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.setMargins(0,0,0,0)
                binding.dateTextView.layoutParams = layoutParams

                if(isSaturday(date)){
                    val newLayoutParams = binding.dateTextView.layoutParams as ViewGroup.MarginLayoutParams
                    newLayoutParams.setMargins(0, 0, 0, 16.dpToPx(itemView.context)) // Adjust the margin as needed
                    binding.dateTextView.layoutParams = newLayoutParams
                }
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

