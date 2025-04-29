package com.example.tes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler

class SplashActivity : Activity() {
    var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashfile)

        handler = Handler()
        handler!!.postDelayed({
            val intent = Intent(
                this@SplashActivity,
                MainActivity::class.java
            )
            startActivity(intent)
            finish()
        }, 3000)
    }
}

