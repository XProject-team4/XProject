package com.zartre.app.beacondemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val EXTRA_BEACON = "beacon_uuid"
    var temp="temp"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        temp = intent.getStringExtra("allowed_area").toString()
        Log.d("TEST", temp)
    }

    fun sendUuid(view: View) {
//        val input = findViewById<EditText>(R.id.inp_uuid)
        val input = inp_uuid.text.toString()
        Log.d("CHECK", input)
        var output = "output"
        val area_arr = temp.split(",")
        Log.d("!!!!!!!!!!!!!!!!!!!!!!!!!", "array : " + area_arr)
        for (area in area_arr){
            val temp_arr = area.split(":")

            Log.d("CHECK","")
            if(temp_arr[0] == input){
                Log.d("output", ":"+temp_arr[1])
                output = temp_arr[1]
            }
            Log.d("!!!!!!!!TEST!!!!!!!!!", "id : " + output)
        }
        val uuidStr = output
        val intent = Intent(this, ScannerActivity::class.java).apply {
            putExtra(EXTRA_BEACON, uuidStr)
        }
        startActivity(intent)
    }

}

//fun sendUuid(view: View) {
//    val input = findViewById<EditText>(R.id.inp_uuid)
//
//
//    val uuidStr = input.text.toString()
//    val intent = Intent(this, ScannerActivity::class.java).apply {
//        putExtra(EXTRA_BEACON, uuidStr)
//    }
//    startActivity(intent)
//}
