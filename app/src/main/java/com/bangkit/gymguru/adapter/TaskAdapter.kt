package com.bangkit.gymguru.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.gymguru.R
import com.bangkit.gymguru.data.TaskView
import org.w3c.dom.Text

class TaskAdapter(private var tasks: List<TaskView>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.date)
        val taskNameTextView: TextView = itemView.findViewById(R.id.tasks_name)
        val timeStartTextView: TextView = itemView.findViewById(R.id.time_start)
        val timeEndTextView: TextView = itemView.findViewById(R.id.time_end)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position]

        holder.dateTextView.text = currentTask.date
        holder.taskNameTextView.text = currentTask.tow
        holder.timeStartTextView.text = currentTask.time_start
        holder.timeEndTextView.text = currentTask.time_end

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(currentTask)
        }
    }

    override fun getItemCount() = tasks.size

    fun setTasks(tasks: List<TaskView>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(task: TaskView)
    }
}