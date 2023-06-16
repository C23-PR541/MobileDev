package com.bangkit.gymguru.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bangkit.gymguru.R
import com.bangkit.gymguru.databinding.ActivityEditTaskBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class EditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditTaskBinding
    private var selectedTow: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tow = intent.getStringExtra("tow")
        val timeStart = intent.getStringExtra("time_start")
        val timeEnd = intent.getStringExtra("time_end")
        val notes = intent.getStringExtra("notes")
        val taskId = intent.getStringExtra("taskId")

        val textInputEditTextStart = binding.tedTimeStart
        val placeholderStart = "00:00"
        textInputEditTextStart.hint = placeholderStart

        val textInputEditTextEnd = binding.tedTimeTwo
        val placeholderEnd = "23:59"
        textInputEditTextEnd.hint = placeholderEnd

        binding.tedTimeStart.setText(timeStart)
        binding.tedTimeTwo.setText(timeEnd)
        binding.tedNotes.setText(notes)

        // Set up the spinner
        val towArray = resources.getStringArray(R.array.Tow)
        val selectedTowPosition = towArray.indexOf(tow)
        val towAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, towArray)
        towAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.towSpinner.adapter = towAdapter
        binding.towSpinner.setSelection(selectedTowPosition)

        binding.towSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedTow = towArray[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
            }
        }

        binding.btnSave.setOnClickListener {
            val newTimeStart = binding.tedTimeStart.text.toString()
            val newTimeEnd = binding.tedTimeTwo.text.toString()
            val newNotes = binding.tedNotes.text.toString()

            if (newTimeStart.isEmpty() || newTimeEnd.isEmpty() || newNotes.isEmpty()) {
                Toast.makeText(
                    this@EditTaskActivity,
                    "Please input all data",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Validate the time format
            if (!isValidTime(newTimeStart) || !isValidTime(newTimeEnd)) {
                Toast.makeText(
                    this@EditTaskActivity,
                    "Please enter a valid time format : \n" +
                            "(00:00 - 23:59)",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // Convert time strings to Date objects
            val startTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(newTimeStart)
            val endTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(newTimeEnd)

            if (startTime != null && endTime != null && startTime.after(endTime)) {
                Toast.makeText(
                    this@EditTaskActivity,
                    "Start time cannot be greater than end time",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Get the reference to the specific task in the database
            val taskReference = FirebaseDatabase.getInstance().reference
                .child("tasks")
                .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                .child(taskId!!)

            // Create a HashMap with the updated task data
            val updatedTaskData = HashMap<String, Any>()
            updatedTaskData["tow"] = selectedTow
            updatedTaskData["time_start"] = newTimeStart
            updatedTaskData["time_end"] = newTimeEnd
            updatedTaskData["notes"] = newNotes

            // Update the task data in the database
            taskReference.updateChildren(updatedTaskData)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@EditTaskActivity,
                            "Task updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this, DetailCalendarActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@EditTaskActivity,
                            "Failed to update task",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun isValidTime(time: String): Boolean {
        val pattern = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$".toRegex()
        return time.matches(pattern)
    }
}