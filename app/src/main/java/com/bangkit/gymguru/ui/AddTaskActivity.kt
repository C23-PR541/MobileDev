package com.bangkit.gymguru.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bangkit.gymguru.R
import com.bangkit.gymguru.databinding.ActivityAddTaskBinding

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private var isTowSelected = false
    private var previousSelectedPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tow = resources.getStringArray(R.array.Tow)

        val spinner = binding.towSpinner
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, tow)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    if (isTowSelected) {
                        if (position == 0) {
                            spinner.setSelection(previousSelectedPosition) // Set the spinner to the previous selected position
                        } else {
                            previousSelectedPosition = position // Update the previous selected position
                        }
                    } else {
                        isTowSelected = true
                        previousSelectedPosition = position // Set the initial selected position
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }
}