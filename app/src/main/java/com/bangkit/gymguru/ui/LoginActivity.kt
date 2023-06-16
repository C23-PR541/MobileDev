package com.bangkit.gymguru.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bangkit.gymguru.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()

        binding.btnLogin.setOnClickListener {
            performLogIn()
        }

        binding.forgotPw.setOnClickListener {
            startActivity(Intent(this, VerificationTwoActivity::class.java))
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun performLogIn() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid

                    val databaseReference = database.reference.child("users").child(uid!!)
                    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val name = snapshot.child("name").value?.toString()

                            // Save UID and name to SharedPreferences
                            saveUserInfo(uid, name)

                            startActivity(Intent(this@LoginActivity, CalendarActivity::class.java))

                            Toast.makeText(
                                baseContext,
                                "Login Success.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                baseContext,
                                "Error occurred ${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                } else {
                    Toast.makeText(
                        baseContext,
                        "Login failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
                .addOnFailureListener{
                    Toast.makeText(
                        this, "Error occurred ${it.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun saveUserInfo(uid: String?, name: String?) {
        val prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("uid", uid)
        editor.putString("name", name)
        editor.apply()
    }
}