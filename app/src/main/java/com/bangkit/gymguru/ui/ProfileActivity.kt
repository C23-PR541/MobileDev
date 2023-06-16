package com.bangkit.gymguru.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.gymguru.R
import com.bangkit.gymguru.data.Users
import com.bangkit.gymguru.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Database
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()

        prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        binding.viewProfileContainer.setOnClickListener {
            startActivity(Intent(this, ViewProfileActivity::class.java))
        }

        binding.historyContainer.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.logoutContainer.setOnClickListener {
            val editor = prefs.edit()
            editor.putString("uid", null)
            editor.putString("name", null)
            editor.apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_view)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_menu -> {
                    startActivity(Intent(this, CalendarActivity::class.java))
                    // Handle home menu item click
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
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Set the profile menu item as selected
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_view)
        bottomNavigation.menu.findItem(R.id.profile_menu).isChecked = true

        // Fetch user's data and display the name
        val currentUser = auth.currentUser
        val currentUserEmail = currentUser?.email

        currentUserEmail?.let { email ->
            val databaseReference = database.reference.child("users")
            val query = databaseReference.orderByChild("email").equalTo(email)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(Users::class.java)
                        user?.let {
                            val name = user.name
                            val email = user.email
                            binding.username.text = name
                            binding.email.text = email
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }
}