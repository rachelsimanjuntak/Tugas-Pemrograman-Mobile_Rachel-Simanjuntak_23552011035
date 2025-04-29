package com.example.tes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DashboardRegis : AppCompatActivity() {
    private lateinit var idwelcome: TextView
    private lateinit var iduser: TextView
    private lateinit var idemail: TextView
    private lateinit var idpassword: TextView
    private lateinit var idgender: TextView
    private lateinit var idbirthdatePicker: TextView
    private lateinit var idaddressText: TextView
    private lateinit var idShare: TextView
    private lateinit var btnShowProfile: Button

    private var email: String? = null
    private var password: String? = null
    private var name: String? = null
    private var birthdate: String? = null
    private var gender: String? = null
    private var address: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.dashboard)

        // Inisialisasi tampilan
        initializeViews()

        // Ambil data dari intent
        getIntentData()

        // Tampilkan informasi pengguna
        displayUserData()

        // Atur aksi klik
        setupClickListeners()

        // Tampilkan toast sambutan
        Toast.makeText(applicationContext, "Selamat datang $name!", Toast.LENGTH_SHORT).show()

        // Atur insets tampilan
        setupWindowInsets()
    }

    private fun initializeViews() {
        idwelcome = findViewById(R.id.idwelcome)
        iduser = findViewById(R.id.iduser)
        idemail = findViewById(R.id.idemail)
        idpassword = findViewById(R.id.idpassword)
        idgender = findViewById(R.id.idgender)
        idbirthdatePicker = findViewById(R.id.idbirthdatePicker)
        idaddressText = findViewById(R.id.idaddressText)
        idShare = findViewById(R.id.idShare)
        btnShowProfile = findViewById(R.id.btnShowProfile)
    }

    private fun getIntentData() {
        email = intent.getStringExtra("EMAIL")
        password = intent.getStringExtra("PASSWORD")
        name = intent.getStringExtra("NAME")
        birthdate = intent.getStringExtra("BIRTHDATE")
        gender = intent.getStringExtra("GENDER")
        address = intent.getStringExtra("ADDRESS")
    }

    private fun displayUserData() {
        idwelcome.text = "Halo, $name"
        iduser.text = name
        idemail.text = email
        idgender.text = "Jenis Kelamin: $gender"
        idbirthdatePicker.text = "Tanggal Lahir: $birthdate"
        idaddressText.text = "Alamat: $address"
    }

    private fun setupClickListeners() {
        idemail.setOnClickListener {
            if (email != null) {
                openEmailClient(email!!)
            } else {
                Toast.makeText(applicationContext, "Tidak ada email yang tersedia", Toast.LENGTH_SHORT).show()
            }
        }

        idaddressText.setOnClickListener {
            openMaps(address ?: "")
        }

        idShare.setOnClickListener {
            shareUserData(name ?: "", email ?: "", gender ?: "", birthdate ?: "", address ?: "")
        }

        btnShowProfile.setOnClickListener {
            showProfileToast()
        }
    }

    private fun showProfileToast() {
        val profileInfo = """
            Ringkasan Profil:
            Nama: $name
            Email: $email
            Jenis Kelamin: $gender
            Tanggal Lahir: $birthdate
            Alamat: $address
        """.trimIndent()

        Toast.makeText(this, profileInfo, Toast.LENGTH_LONG).show()
    }

    private fun openEmailClient(email: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, "Halo $name")
            putExtra(Intent.EXTRA_TEXT, "Ini adalah pesan dari aplikasi profil Anda.")
        }

        if (emailIntent.resolveActivity(packageManager) != null) {
            Toast.makeText(applicationContext, "Membuka aplikasi email", Toast.LENGTH_SHORT).show()
            startActivity(emailIntent)
        } else {
            Toast.makeText(applicationContext, "Tidak ditemukan aplikasi email", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openMaps(address: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (mapIntent.resolveActivity(packageManager) != null) {
            Toast.makeText(applicationContext, "Membuka Google Maps dengan alamat: $address", Toast.LENGTH_SHORT).show()
            startActivity(mapIntent)
        } else {
            Toast.makeText(applicationContext, "Aplikasi Google Maps tidak ditemukan", Toast.LENGTH_SHORT).show()
            val browserMapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=${Uri.encode(address)}"))
            startActivity(browserMapIntent)
        }
    }

    private fun shareUserData(name: String, email: String, gender: String, birthdate: String, address: String) {
        val message = """
            Halo! Saya ingin membagikan informasi saya:
            Nama: $name
            Email: $email
            Jenis Kelamin: $gender
            Tanggal Lahir: $birthdate
            Alamat: $address
        """.trimIndent()

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Informasi Profil Saya")
            putExtra(Intent.EXTRA_TEXT, message)
        }

        Toast.makeText(applicationContext, "Membagikan informasi profil", Toast.LENGTH_SHORT).show()
        startActivity(Intent.createChooser(shareIntent, "Bagikan via"))
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dashboard)) { v: View, insets: WindowInsetsCompat ->
            val systemBars: Insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
