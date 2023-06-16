package com.bangkit.gymguru.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bangkit.gymguru.data.Users
import com.bangkit.gymguru.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()

        binding.btnRegister.setOnClickListener {
            if(binding.etConfirmPassword.text.toString() == binding.etPassword.text.toString()) {
                performSignUp()
            } else {
                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun performSignUp() {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    // For Realtime Database
                    val databaseReference = database.reference.child("users").child(auth.currentUser!!.uid)
                    val users: Users = Users(name, email, auth.currentUser!!.uid, null, null, null, null)
                    databaseReference.setValue(users).addOnCompleteListener{
                        if (it.isSuccessful) {
                            startActivity(Intent(this, LoginActivity::class.java))
                            Toast.makeText(
                                baseContext,
                                "Authentication Success.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(this, "Error occurred ${it.exception}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // If sign-in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
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