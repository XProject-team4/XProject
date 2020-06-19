package com.zartre.app.beacondemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

//          * 소상공인 (사업자)
            var intent = Intent(applicationContext, unlock::class.java)
            startActivity(intent)
//          * 게스트
//              var intent = Intent(applicationContext, qr_check::class.java)
//              startActivity(intent)

        }
    }
}



// 1. 비콘 스캔
// 2. 비콘 스캔한 id랑 서버에서 받은 비콘 id 비교
// 2-1. 같으면 서버에 "True, 비콘 id" 전송
// 2-2. 다르면 서버에 보내는거 없이 dialog msg만 띄우기