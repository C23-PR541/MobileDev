package com.bangkit.gymguru.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bangkit.gymguru.R
import com.bangkit.gymguru.data.Task
import com.bangkit.gymguru.databinding.ActivityAddTaskBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()

        val currentUserId = auth.currentUser?.uid

        val tow = resources.getStringArray(R.array.Tow)
        val selectedMonth = intent.getStringExtra("selectedMonth")
        val selectedDate = intent.getStringExtra("selectedDate")

        val spinner = binding.towSpinner
        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tow)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val selectedTow = parent.getItemAtPosition(position) as String
                    Log.d("AddTaskActivity", "Selected tow: $selectedTow")
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        binding.btnAdd.setOnClickListener {
            val month = selectedMonth.toString()
            val date = selectedDate.toString()
            val tow = spinner.selectedItem as String
            val time_start = binding.tedTimeStart.text.toString()
            val time_end = binding.tedTimeTwo.text.toString()
            val notes = binding.tedNotes.text.toString()

            currentUserId?.let { userId ->
                val taskReference = database.reference.child("tasks").child(userId).push()
                val taskId = taskReference.key

                val taskData = HashMap<String, Any>()
                taskData["month"] = month
                taskData["date"] = date
                taskData["tow"] = tow
                taskData["time_start"] = time_start
                taskData["time_end"] = time_end
                taskData["notes"] = notes

                taskId?.let {
                    taskReference.setValue(taskData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@AddTaskActivity,
                                    "Task added successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            } else {
                                Toast.makeText(
                                    this@AddTaskActivity,
                                    "Failed to add task",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }
}