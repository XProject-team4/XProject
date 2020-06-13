package com.example.smartkey_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_qr_check.*

class qr_check : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_check)

        qr_check_btn.setOnClickListener {
            var intent = Intent(applicationContext, unlock::class.java)
            startActivity(intent)
        }
    }
}
