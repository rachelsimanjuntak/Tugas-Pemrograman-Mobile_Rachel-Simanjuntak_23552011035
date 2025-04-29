package com.example.tes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.DatePicker
import android.widget.DatePicker.OnDateChangedListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private var idemail: EditText? = null
    private var idpassword: EditText? = null
    private var idbirthdate: EditText? = null
    private var idname: EditText? = null
    private var idaddress: EditText? = null

    private var eyeIcon: ImageView? = null
    private var idmale: RadioButton? = null
    private var idfemale: RadioButton? = null
    private var idbutton: Button? = null
    private var idbirthdatePicker: DatePicker? = null
    private var selectedDate = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        idpassword.setOnTouchListener(object : OnTouchListener {
            var isPasswordVisible: Boolean = false

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (idpassword.getRight() - idpassword.getCompoundDrawables()[2].bounds.width())) {
                        // Touch pada ikon mata
                        if (isPasswordVisible) {
                            // Sembunyikan password
                            idpassword.setInputType(129)
                            eyeIcon.setImageResource(R.drawable.mataoff)
                            isPasswordVisible = false
                        } else {
                            // Tampilkan password
                            idpassword.setInputType(144)
                            eyeIcon.setImageResource(R.drawable.mataon)
                            isPasswordVisible = true
                        }
                        // Pindahkan kursor ke akhir teks
                        idpassword.setSelection(idpassword.getText().length)
                        return true
                    }
                }
                return false
            }
        })


        idbirthdatePicker.init(
            Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH],
            Calendar.getInstance()[Calendar.DAY_OF_MONTH],
            OnDateChangedListener { datePicker, year, monthOfYear, dayOfMonth -> // Simpan tanggal yang dipilih
                selectedDate = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                // Tetapkan tanggal ke EditText idbirthdate
                idbirthdate.setText(selectedDate)
            })

        idbutton.setOnClickListener(View.OnClickListener {
            // Ambil nilai dari semua input
            val email = idemail.getText().toString().trim { it <= ' ' }
            val password = idpassword.getText().toString().trim { it <= ' ' }
            val name = idname.getText().toString().trim { it <= ' ' }
            val birthdate = idbirthdate.getText().toString().trim { it <= ' ' }
            val address = idaddress.getText().toString().trim { it <= ' ' }
            val gender =
                if (idmale.isChecked()) "Male" else (if (idfemale.isChecked()) "Female" else "")

            // Validasi input
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(
                    name
                ) ||
                TextUtils.isEmpty(birthdate) || TextUtils.isEmpty(address) || TextUtils.isEmpty(
                    email
                ) ||
                TextUtils.isEmpty(gender)
            ) {
                // Jika ada input yang kosong, tampilkan pesan kesalahan
                Toast.makeText(
                    this@MainActivity,
                    "Please Fill All!!!.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Jika semua input sudah diisi, lakukan validasi tanggal
                if (!isDateValid(birthdate)) {
                    // Jika tanggal tidak valid, tampilkan pesan kesalahan
                    Toast.makeText(
                        this@MainActivity,
                        "Birthdate must be 10 years ago from today.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Jika semua input valid, proses data
                    // Buat intent untuk berpindah ke DetailActivity
                    val intent = Intent(
                        this@MainActivity,
                        DashboardRegis::class.java
                    )
                    intent.putExtra("EMAIL", email)
                    intent.putExtra("PASSWORD", password)
                    intent.putExtra("NAME", name)
                    intent.putExtra("GENDER", gender)
                    intent.putExtra("BIRTHDATE", birthdate)
                    intent.putExtra("ADDRESS", address)
                    // Kirim selectedDate sebagai ekstra intent
                    intent.putExtra("SELECTED_DATE", selectedDate)
                    startActivity(intent)
                }
            }
        })
    }

    // Metode untuk memeriksa apakah tanggal yang dipilih adalah 10 tahun yang lalu dari hari ini
    private fun isDateValid(selectedDate: String): Boolean {
        // Mendapatkan tanggal saat ini
        val currentDate = Calendar.getInstance()
        // Mendapatkan tanggal 10 tahun yang lalu dari hari ini
        currentDate.add(Calendar.YEAR, -10)
        // Mendapatkan tanggal dari input birthdate
        val dateParts =
            selectedDate.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val day = dateParts[0].toInt()
        val month = dateParts[1].toInt() - 1 // Calendar.MONTH dimulai dari 0
        val year = dateParts[2].toInt()

        // Mendapatkan tanggal dari input birthdate
        val selectedDateCalendar = Calendar.getInstance()
        selectedDateCalendar[year, month] = day

        // Memeriksa apakah tanggal yang dipilih adalah 10 tahun yang lalu dari hari ini
        return selectedDateCalendar.before(currentDate)
    }
}
