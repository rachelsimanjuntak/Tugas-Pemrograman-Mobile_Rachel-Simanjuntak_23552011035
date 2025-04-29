package com.example.tes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardRegis : AppCompatActivity() {
    private var idwelcome: TextView? = null
    private var idemail: TextView? = null
    private var idgender: TextView? = null
    private var idpassword: TextView? = null
    private var idbirthdatePicker: TextView? = null
    private var idaddressText: TextView? = null
    private var idShare: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        // Inisialisasi UI
        idwelcome = findViewById(R.id.idwelcome)
        idemail = findViewById(R.id.idemail)
        idgender = findViewById(R.id.idgender)
        idpassword = findViewById(R.id.idpassword)
        idbirthdatePicker = findViewById(R.id.idbirthdatePicker)
        idaddressText = findViewById(R.id.idaddressText)
        idShare = findViewById(R.id.idShare) // ID TextView untuk tombol share

        // Menerima data dari Intent
        val intent = intent
        if (intent != null) {
            val name = intent.getStringExtra("NAME")
            val email = intent.getStringExtra("EMAIL")
            val gender = intent.getStringExtra("GENDER")
            val password = intent.getStringExtra("PASSWORD")
            val birthdate = intent.getStringExtra("BIRTHDATE")
            val address = intent.getStringExtra("ADDRESS")

            // Menampilkan data pada TextView
            idwelcome.setText("Welcome, $name")
            idemail.setText(email)
            idgender.setText(gender)
            idpassword.setText(password)
            idbirthdatePicker.setText(birthdate)
            idaddressText.setText(address)

            // Menambahkan onClickListener ke TextView email
            idemail.setOnClickListener(View.OnClickListener { // Intent implicit untuk membuka aplikasi email
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.setData(Uri.parse("mailto:$email"))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body")
                if (emailIntent.resolveActivity(packageManager) != null) {
                    startActivity(emailIntent)
                }
            })

            // Menambahkan onClickListener ke TextView address
            idaddressText.setOnClickListener(View.OnClickListener { // Intent implicit untuk membuka Google Maps
                val gmmIntentUri =
                    Uri.parse("geo:0,0?q=" + Uri.encode(address))
                val mapIntent =
                    Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent)
                }
            })

            // Menambahkan onClickListener ke TextView share
            idShare.setOnClickListener(View.OnClickListener { // Membuat pesan yang ingin dibagikan
                val pesannich = """
                    Hello! I am sharing my information:
                    Name: $name
                    Email: $email
                    Gender: $gender
                    Birthdate: $birthdate
                    Address: $address
                    """.trimIndent()

                // Intent untuk berbagi teks
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.setType("text/plain")
                shareIntent.putExtra(
                    Intent.EXTRA_SUBJECT,
                    "My Information"
                ) // Subjek pesan
                shareIntent.putExtra(Intent.EXTRA_TEXT, pesannich) // Isi pesan

                // Menampilkan dialog berbagi
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            })
        }
    }
}
