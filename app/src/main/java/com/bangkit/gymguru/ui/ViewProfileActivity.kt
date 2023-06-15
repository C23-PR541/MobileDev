package com.bangkit.gymguru.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bangkit.gymguru.R
import com.bangkit.gymguru.data.Users
import com.bangkit.gymguru.databinding.ActivityViewProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ViewProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private var selectedGender: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()

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
                            binding.etName.setText(name)

                            val age = user.age
                            if (age != null) {
                                binding.etAge.setText(age.toString())
                            } else {
                                binding.etAge.setText("")
                            }

                            val weight = user.weight
                            if (weight != null) {
                                binding.etWeight.setText(weight.toString())
                            } else {
                                binding.etWeight.setText("")
                            }

                            val height = user.height
                            if (height != null) {
                                binding.etHeight.setText(height.toString())
                            } else {
                                binding.etHeight.setText("")
                            }

                            val gender = user.gender
                            if (gender != null) {
                                selectedGender = gender
                                val genderPosition = if (gender == "Female") 0 else 1
                                binding.genderSpinner.setSelection(genderPosition)
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }

        val genders = resources.getStringArray(R.array.Gender)

        val spinner = binding.genderSpinner
        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    selectedGender = parent.getItemAtPosition(position) as String
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    Toast.makeText(this@ViewProfileActivity, "Please select gender", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btnSave.setOnClickListener {
            val newName = binding.etName.text.toString()
            val newAge = binding.etAge.text.toString().toIntOrNull()
            val newWeight = binding.etWeight.text.toString().toIntOrNull()
            val newHeight = binding.etHeight.text.toString().toIntOrNull()

            currentUserEmail?.let { email ->
                val databaseReference = database.reference.child("users")
                val query = databaseReference.orderByChild("email").equalTo(email)

                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (userSnapshot in dataSnapshot.children) {
                            val userId = userSnapshot.key
                            userId?.let {
                                val updatedUserData = mutableMapOf<String, Any>()
                                updatedUserData["name"] = newName
                                newAge?.let { age ->
                                    updatedUserData["age"] = age
                                }
                                newWeight?.let { weight ->
                                    updatedUserData["weight"] = weight
                                }
                                newHeight?.let { height ->
                                    updatedUserData["height"] = height
                                }

                                updatedUserData["gender"] = selectedGender

                                databaseReference.child(userId).updateChildren(updatedUserData)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                this@ViewProfileActivity,
                                                "Profile updated successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                this@ViewProfileActivity,
                                                "Failed to update profile",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
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
}