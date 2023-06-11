package com.bangkit.gymguru.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bangkit.gymguru.databinding.ActivityVerificationTwoBinding
import com.google.firebase.auth.FirebaseAuth

class VerificationTwoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityVerificationTwoBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnVerify.setOnClickListener {
            auth.sendPasswordResetEmail(binding.edOtpCode.text.toString())
                .addOnCompleteListener {
                    Toast.makeText(this, "Email sent. Please check your Email!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Error occurred ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}