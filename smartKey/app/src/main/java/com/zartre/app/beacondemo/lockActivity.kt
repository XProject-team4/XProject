package com.zartre.app.beacondemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_lock.*



class lockActivity : AppCompatActivity() {
    private val USERINFO = "UserID"
    private val EXTRA_BEACON = "beacon_uuid"
    private val ALLOWED = "allowed_area"
    private val ID = "id"

    var temp="temp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)

//        var u_id = intent.getStringExtra(USERINFO).toString()
//        Log.d("USERID", u_id)

        temp = intent.getStringExtra(ALLOWED).toString()
        Log.d("TEST", temp)

        qr_btn.setOnClickListener {
            var intent = Intent(applicationContext, qr_check::class.java).apply {
//                putExtra(USERINFO, u_id)
            }
            startActivity(intent)
        }

        lock_btn.setOnClickListener{
            val input = entry.text.toString()
            Log.d("CHECK", input)
            var output = "output"
            val area_arr = temp.split(",")
            Log.d("area ", "array : " + area_arr)
            for (area in area_arr){
                val temp_arr = area.split(":")
                Log.d("CHECK","")
                if(temp_arr[0] == input){
                    Log.d("uuid split output", ":"+temp_arr[1])
                    output = temp_arr[1]
                }
                Log.d("real uuid", "id : " + output)
            }
            val uuidStr = output
            Log.d("UUIDSTR", uuidStr)
            val allow = temp
            Log.d("ALLOW", allow)
            val intent = Intent(this, ScannerActivity::class.java).apply {
                putExtra(EXTRA_BEACON, uuidStr)
                putExtra(ID, "0")
            }
            startActivity(intent)
        }

        var area1 = findViewById<TextView>(R.id.entry1)
        var area2 = findViewById<TextView>(R.id.entry2)
//        var area3 = findViewById<TextView>(R.id.entry3)

        val area_arr = temp.split(",") // [화장실:11, 주차장:22]
        area1.setText(area_arr[0].split(":")[0]) //[화장실, 11]
        area2.setText(area_arr[1].split(":")[0])
//        area3.setText(area_arr[2].split(":")[0])
    }
}
//    fun sendUuid(view: View) {
////        val input = findViewById<EditText>(R.id.inp_uuid)
//        val input = entry.text.toString()
//        Log.d("CHECK", input)
//        var output = "output"
//        val area_arr = temp.split(",")
//        Log.d("!!!!!!!!!!!!!!!!!!!!!!!!!", "array : " + area_arr)
//        for (area in area_arr){
//            val temp_arr = area.split(":")
//
//            Log.d("CHECK","")
//            if(temp_arr[0] == input){
//                Log.d("output", ":"+temp_arr[1])
//                output = temp_arr[1]
//            }
//            Log.d("!!!!!!!!TEST!!!!!!!!!", "id : " + output)
//        }
//        val uuidStr = output
//        val intent = Intent(this, ScannerActivity::class.java).apply {
//            putExtra(EXTRA_BEACON, uuidStr)
//        }
//        startActivity(intent)
//    }



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
