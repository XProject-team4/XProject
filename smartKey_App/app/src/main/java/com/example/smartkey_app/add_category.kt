package com.example.smartkey_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_category.*

class add_category : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        var textView = findViewById<View>(R.id.dddd) as TextView
        var spinner = findViewById<View>(R.id.spinner) as Spinner

        spinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                textView.setText(parent.getItemAtPosition(position).toString() + " 이(가) 맞나요?")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        register_btn.setOnClickListener {
            var intent = Intent(applicationContext, lock::class.java)
            startActivity(intent)
        }
    }
}
