package com.bangkit.gymguru.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.gymguru.R
import com.bangkit.gymguru.data.HistoryItem
import com.bangkit.gymguru.data.TaskView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class HistoryAdapter(private var historyList: List<HistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemNametextView : TextView = itemView.findViewById(R.id.item_name)
        val itemDateTextView : TextView = itemView.findViewById(R.id.item_date)
        val itemImageView : ImageView = itemView.findViewById(R.id.item_pic)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.itemNametextView.text = item.resViewText
        holder.itemDateTextView.text = item.date
        Glide.with(holder.itemImageView.context)
            .load(item.imageUrl)
            .transform(RoundedCorners(20)) // Adjust the radius as desired
            .into(holder.itemImageView)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    fun setHistory(history: List<HistoryItem>) {
        this.historyList = history
        notifyDataSetChanged()
    }
}