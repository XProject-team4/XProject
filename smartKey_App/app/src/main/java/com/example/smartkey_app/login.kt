package com.example.smartkey_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val button = findViewById<View>(R.id.signUp_btn) as Button
        button.setOnClickListener {
            val intent = Intent(applicationContext, signup::class.java)
            startActivity(intent)
        }

//        val button1 =
//            findViewById<View>(R.id.login_btn) as Button
//        button1.setOnClickListener {
//            val intent = Intent(applicationContext, lock::class.java)
//            startActivity(intent)
//        }


    }
}

