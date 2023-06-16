package com.bangkit.gymguru.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.gymguru.R
import com.bangkit.gymguru.adapter.DateAdapter
import com.bangkit.gymguru.adapter.DaysOfWeekAdapter
import com.bangkit.gymguru.adapter.TaskAdapter
import com.bangkit.gymguru.data.Task
import com.bangkit.gymguru.data.TaskView
import com.bangkit.gymguru.databinding.ActivityDetailCalendarBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class DetailCalendarActivity : AppCompatActivity(), DateAdapter.DateClickListener, TaskAdapter.OnItemClickListener {

    private lateinit var binding: ActivityDetailCalendarBinding
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var rvTask: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var dateAdapter: DateAdapter
    private val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendarRecyclerView = binding.calendarRecyclerView
        calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)

        rvTask = binding.rvTask
        rvTask.layoutManager = LinearLayoutManager(this)

        val selectedMonthPosition = intent.getIntExtra("selectedMonthPosition", 0)
        val selectedMonth = months[selectedMonthPosition]
        val year = 2023

        // Retrieve the dates for the selected month and year
        val dates = generateDatesForMonth(year, selectedMonthPosition)

        dateAdapter = DateAdapter(this)
        dateAdapter.setDates(dates)

        binding.monthTitle.text = selectedMonth

        binding.daysOfWeekRecyclerView.layoutManager = GridLayoutManager(this, 7)
        binding.daysOfWeekRecyclerView.adapter = DaysOfWeekAdapter(listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"))

        calendarRecyclerView.adapter = dateAdapter

        Toast.makeText(this, "Click the dates to add Tasks", Toast.LENGTH_SHORT).show()

        taskAdapter = TaskAdapter(emptyList(), this) // Initialize with an empty list
        rvTask.adapter = taskAdapter

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_view)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_menu -> {
                    // Handle home menu item click
                    startActivity(Intent(this, CalendarActivity::class.java))
                    true
                }
                R.id.camera_menu -> {
                    // Handle camera menu item click
                    startActivity(Intent(this, ClassifyActivity::class.java))
                    true
                }
                R.id.history_menu -> {
                    // Handle history menu item click
                    startActivity(Intent(this, CalculatorActivity::class.java))
                    true
                }
                R.id.profile_menu -> {
                    // Handle profile menu item click
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }

        retrieveTasksFromDatabase()
    }

    override fun onItemClick(task: TaskView) {
        val intent = Intent(this, TaskDetailActivity::class.java)
        intent.putExtra("date", task.date)
        intent.putExtra("tow", task.tow)
        intent.putExtra("time_start", task.time_start)
        intent.putExtra("time_end", task.time_end)
        intent.putExtra("month", binding.monthTitle.text.toString())
        intent.putExtra("notes", task.notes)
        intent.putExtra("taskId", task.taskId)
        intent.putExtra("year", "2023")
        startActivity(intent)
    }

    private fun retrieveTasksFromDatabase() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val tasksReference = currentUserId?.let {
            FirebaseDatabase.getInstance().reference.child("tasks").child(it)
        }

        if (tasksReference != null) {
            tasksReference.orderByChild("date").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val taskList = mutableListOf<TaskView>()
                    val selectedMonthPosition = intent.getIntExtra("selectedMonthPosition", 0)
                    val selectedMonth = months[selectedMonthPosition]

                    for (taskSnapshot in snapshot.children) {
                        val taskData = taskSnapshot.getValue(Task::class.java)
                        taskData?.let {
                            val taskMonth = it.month

                            if (taskMonth == selectedMonth) {
                                val taskView = TaskView(
                                    it.date,
                                    it.tow,
                                    it.time_start,
                                    it.time_end,
                                    it.notes,
                                    it.taskId
                                )
                                taskList.add(taskView)
                            }
                        }
                    }

                    // Sort the taskList based on date
                    taskList.sortBy { it.date }

                    taskAdapter.setTasks(taskList)
                    taskAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@DetailCalendarActivity,
                        "Failed to retrieve tasks",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    override fun onDateClicked(date: String) {
        val selectedMonthPosition = intent.getIntExtra("selectedMonthPosition", 0)
        val selectedMonth = months[selectedMonthPosition]

        val intent = Intent(this, AddTaskActivity::class.java)
        intent.putExtra("selectedMonth", selectedMonth)
        intent.putExtra("selectedDate", date)
        startActivity(intent)
    }

    private fun generateDatesForMonth(year: Int, month: Int): List<String> {
        val dates = mutableListOf<String>()
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 2)
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val offset = if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - 2

        for (i in 0 until offset) {
            dates.add("")
        }

        for (day in 1..daysInMonth) {
            calendar.set(year, month, day)
            val date = dateFormat.format(calendar.time)
            dates.add(date)
        }
        return dates
    }
}