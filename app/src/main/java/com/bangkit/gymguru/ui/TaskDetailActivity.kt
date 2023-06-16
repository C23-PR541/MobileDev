package com.bangkit.gymguru.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bangkit.gymguru.databinding.ActivityTaskDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailBinding
    private lateinit var tasksReference: DatabaseReference
    private lateinit var taskId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val date = intent.getStringExtra("date")
        val tow = intent.getStringExtra("tow")
        val timeStart = intent.getStringExtra("time_start")
        val timeEnd = intent.getStringExtra("time_end")
        val month = intent.getStringExtra("month")
        val notes = intent.getStringExtra("notes")
        val year = intent.getStringExtra("year")

        taskId = intent.getStringExtra("taskId").toString()
        tasksReference = FirebaseDatabase.getInstance().reference.child("tasks")
            .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")// Get the task ID

        val taskDate = "$date $month $year"
        val taskTime = "$timeStart - $timeEnd"

        binding.taskDate.text = taskDate
        binding.taskNames.text = tow
        binding.taskTime.text = taskTime
        binding.taskNotes.text = notes

        binding.btnDelete.setOnClickListener {
            deleteTask()
        }
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, EditTaskActivity::class.java).apply {
                putExtra("date", date)
                putExtra("tow", tow)
                putExtra("time_start", timeStart)
                putExtra("time_end", timeEnd)
                putExtra("month", month)
                putExtra("notes", notes)
                putExtra("year", year)
                putExtra("taskId", taskId)
            }
            startActivity(intent)
        }
    }

    private fun deleteTask() {
        tasksReference.child(taskId).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@TaskDetailActivity,
                        "Task deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@TaskDetailActivity,
                        "Failed to delete task",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}