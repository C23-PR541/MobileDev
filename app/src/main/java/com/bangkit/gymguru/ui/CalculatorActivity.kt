package com.bangkit.gymguru.ui

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bangkit.gymguru.R
import com.bangkit.gymguru.databinding.ActivityCalculatorBinding
import java.nio.FloatBuffer

class CalculatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorBinding
    private var selectedGenderValue = 0 // 0 for Female, 1 for Male

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textInputEditTextAge = binding.tedAge
        val placeholderAge = "... Years"
        textInputEditTextAge.inputType = InputType.TYPE_CLASS_NUMBER
        textInputEditTextAge.hint = placeholderAge

        val textInputEditTextWeight = binding.tedWeight
        val placeholderWeight = "... KG"
        textInputEditTextWeight.inputType = InputType.TYPE_CLASS_NUMBER
        textInputEditTextWeight.hint = placeholderWeight

        val textInputEditTextHeight = binding.tedHeight
        val placeholderHeight = "... CM"
        textInputEditTextHeight.inputType = InputType.TYPE_CLASS_NUMBER
        textInputEditTextHeight.hint = placeholderHeight

        val textInputEditTextHours = binding.tedHours
        val placeholderHours = "... Hours"
        textInputEditTextHours.inputType = InputType.TYPE_CLASS_NUMBER
        textInputEditTextHours.hint = placeholderHours

        val textInputEditTextYears = binding.tedYears
        val placeholderYears = "... Years"
        textInputEditTextYears.inputType = InputType.TYPE_CLASS_NUMBER
        textInputEditTextYears.hint = placeholderYears

        val gender = resources.getStringArray(R.array.Gender)

        val spinner = binding.genderSpinner
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, gender)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val selectedGender = parent.getItemAtPosition(position) as String
                    if (selectedGender == "Female") {
                        selectedGenderValue = 0
                    } else if (selectedGender == "Male") {
                        selectedGenderValue = 1
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    Toast.makeText(this@CalculatorActivity, "Please select gender", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btnPredict.setOnClickListener {
            val gender = selectedGenderValue.toFloat()
            val age = textInputEditTextAge.text.toString().toFloatOrNull()
            val weight = textInputEditTextWeight.text.toString().toFloatOrNull()
            val height = textInputEditTextHeight.text.toString().toFloatOrNull()
            val hours = textInputEditTextHours.text.toString().toFloatOrNull()
            val years = textInputEditTextYears.text.toString().toFloatOrNull()

            if (age != null && weight != null && height != null && hours != null && years != null) {
                val bmi = weight / ((height / 100) * (height / 100))
                val inputs = floatArrayOf(gender, age, weight, height, hours, years, bmi)

                val ortEnvironment = OrtEnvironment.getEnvironment()
                val ortSession = createORTSession(ortEnvironment)
                val output = runPrediction(inputs, ortSession, ortEnvironment)
                showOutputPopup(output, bmi)
            } else {
                Toast.makeText(this, "Please fill in all the inputs", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createORTSession( ortEnvironment: OrtEnvironment) : OrtSession {
        val modelBytes = resources.openRawResource( R.raw.model1 ).readBytes()
        return ortEnvironment.createSession( modelBytes )
    }

    private fun runPrediction(input : FloatArray, ortSession: OrtSession , ortEnvironment: OrtEnvironment ) : Long {
        // Get the name of the input node
        val inputName = ortSession.inputNames?.iterator()?.next()
        // Make a FloatBuffer of the inputs
        val floatBufferInputs = FloatBuffer.wrap(input)
        // Create input tensor with floatBufferInputs of shape ( 1 , 1 )
        val inputTensor = OnnxTensor.createTensor(ortEnvironment, floatBufferInputs, longArrayOf(1, 7))
        // Run the model
        val results = ortSession.run( mapOf( inputName to inputTensor ) )
        // Fetch and return the results
        val output = results[0].value as LongArray
        return output[0]
    }

    fun showOutputPopup(output: Long, bmi: Float) {
        val outputText = when (output) {
            0L -> "You are overweight with a BMI value of $bmi. \nDo Weight Loss"
            1L -> "You are underweight with a BMI value of $bmi. \nDo Muscle Up"
            else -> "Unknown"
        }

        // Inflate the custom layout for the popup
        val inflater = layoutInflater
        val popupView = inflater.inflate(R.layout.popup_output, null)

        // Find views within the custom layout
        val tvOutput = popupView.findViewById<TextView>(R.id.tvOutput)
        val btnClose = popupView.findViewById<Button>(R.id.btnClose)

        // Set the output text
        tvOutput.text = "$outputText"

        // Create the dialog builder
        val builder = AlertDialog.Builder(this)
        builder.setView(popupView)

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()

        // Handle button click
        btnClose.setOnClickListener {
            dialog.dismiss() // Close the dialog when the button is clicked
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, CalendarActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }
}