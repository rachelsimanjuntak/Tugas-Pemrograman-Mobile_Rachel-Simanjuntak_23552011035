package com.example.tes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var idemail: EditText
    private lateinit var idpassword: EditText
    private lateinit var idbirthdate: EditText
    private lateinit var idname: EditText
    private lateinit var idaddress: EditText
    private lateinit var eyeIcon: ImageView
    private lateinit var idmale: RadioButton
    private lateinit var idfemale: RadioButton
    private lateinit var idbutton: Button
    private lateinit var idbirthdatePicker: DatePicker
    private var selectedDate = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupPasswordToggle()
        setupDatePicker()
        setupRegisterButton()
    }

    private fun initViews() {
        idemail = findViewById(R.id.idemail)
        idpassword = findViewById(R.id.idpassword)
        idname = findViewById(R.id.idname)
        idmale = findViewById(R.id.idmale)
        idfemale = findViewById(R.id.idfemale)
        idbirthdate = findViewById(R.id.idbirthdate)
        idaddress = findViewById(R.id.idaddress)
        idbutton = findViewById(R.id.idbutton)
        idbirthdatePicker = findViewById(R.id.idbirthdatePicker)
        eyeIcon = findViewById(R.id.id_eye_icon)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupPasswordToggle() {
        idpassword.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (idpassword.right - idpassword.compoundDrawables[2].bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun togglePasswordVisibility() {
        val isPasswordVisible = idpassword.inputType == 144
        if (isPasswordVisible) {
            // Hide password
            idpassword.inputType = 129
            eyeIcon.setImageResource(R.drawable.mataoff)
        } else {
            // Show password
            idpassword.inputType = 144
            eyeIcon.setImageResource(R.drawable.mataon)
        }
        // Move cursor to the end
        idpassword.setSelection(idpassword.text.length)
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()
        idbirthdatePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { _, year, monthOfYear, dayOfMonth ->
            selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
            idbirthdate.setText(selectedDate)
        }
    }

    private fun setupRegisterButton() {
        idbutton.setOnClickListener {
            val email = idemail.text.toString().trim()
            val password = idpassword.text.toString().trim()
            val name = idname.text.toString().trim()
            val birthdate = idbirthdate.text.toString().trim()
            val address = idaddress.text.toString().trim()
            val gender = when {
                idmale.isChecked -> "Male"
                idfemale.isChecked -> "Female"
                else -> ""
            }

            if (validateInputs(email, password, name, birthdate, address, gender)) {
                if (!isDateValid(birthdate)) {
                    Toast.makeText(
                        this,
                        "Birthdate must be at least 10 years ago from today.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    navigateToDashboard(email, password, name, gender, birthdate, address)
                }
            } else {
                Toast.makeText(
                    this,
                    "Please fill all fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun validateInputs(
        email: String,
        password: String,
        name: String,
        birthdate: String,
        address: String,
        gender: String
    ): Boolean {
        return !(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(birthdate) || TextUtils.isEmpty(address) || TextUtils.isEmpty(gender))
    }

    private fun isDateValid(selectedDate: String): Boolean {
        val dateParts = selectedDate.split("/")
        if (dateParts.size != 3) return false

        val day = dateParts[0].toIntOrNull() ?: return false
        val month = (dateParts[1].toIntOrNull() ?: return false) - 1
        val year = dateParts[2].toIntOrNull() ?: return false

        val currentDate = Calendar.getInstance()
        val minDate = Calendar.getInstance().apply { add(Calendar.YEAR, -10) }

        val selectedDateCalendar = Calendar.getInstance().apply {
            set(year, month, day)
        }

        return selectedDateCalendar.before(minDate)
    }

    private fun navigateToDashboard(
        email: String,
        password: String,
        name: String,
        gender: String,
        birthdate: String,
        address: String
    ) {
        Intent(this, DashboardRegis::class.java).apply {
            putExtra("EMAIL", email)
            putExtra("PASSWORD", password)
            putExtra("NAME", name)
            putExtra("GENDER", gender)
            putExtra("BIRTHDATE", birthdate)
            putExtra("ADDRESS", address)
            putExtra("SELECTED_DATE", selectedDate)
            startActivity(this)
        }
    }
}