package com.example.smartkey_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_lock.*

class lock : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)

        plus_btn.setOnClickListener {
            var intent = Intent(applicationContext, add_category::class.java)
            startActivity(intent)
        }

        lock_btn.setOnClickListener {
            var intent = Intent(applicationContext, qr_check::class.java)
            startActivity(intent)
        }
    }
}
