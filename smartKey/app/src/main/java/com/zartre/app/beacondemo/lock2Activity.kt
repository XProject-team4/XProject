package com.zartre.app.beacondemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_lock2.*

class lock2Activity : AppCompatActivity() {
    private val ALLOWEDAREA = "Allowed area after QR"
    private val EXTRA_BEACON = "beacon_uuid"
    private val ID = "id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock2)

        var random_id = intent.getStringExtra(ID).toString()

        var allow_msg = intent.getStringExtra(ALLOWEDAREA).toString()
        Log.d("CHECK", allow_msg)

        lock_btn.setOnClickListener {
            val input = entry.text.toString()
            Log.d("CHECK", input)

            var output = "output"
            var temp_allowarea_arr = allow_msg.split("/")
            var allowarea_arr = temp_allowarea_arr[1].split(",")
            for (area in allowarea_arr) {
                val temp_arr = area.split(":")
                if (temp_arr[0] == input) {
                    Log.d("uuid split output", ":"+temp_arr[1])
                    output = temp_arr[1]
                }
                Log.d("real uuid", "id : " + output)
            }
            val uuidStr = output
            Log.d("UUIDSTR", uuidStr)
            val allow = allow_msg
            Log.d("ALLOW", allow)

            val intent = Intent(this, ScannerActivity::class.java).apply {
                putExtra(EXTRA_BEACON, uuidStr)
                putExtra(ID, random_id)
                Log.d("CHECK", "uuidStr : " + uuidStr + "randomID" + random_id)
            }
            startActivity(intent)
        }

        var area1 = findViewById<TextView>(R.id.entry1)
        var area2 = findViewById<TextView>(R.id.entry2)
        val temp_area_arr = allow_msg.split("/") // [화장실:11, 주차장:22]
        var area_arr = temp_area_arr[1].split(",")
        area1.setText(area_arr[0].split(":")[0]) //[화장실, 11]
        area2.setText(area_arr[1].split(":")[0])
    }
}
