package com.example.smartkey_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.finishAffinity
import kotlinx.android.synthetic.main.activity_lock.*
import kotlinx.android.synthetic.main.activity_unlock.*
import kotlinx.android.synthetic.main.activity_unlock.lock_btn

class unlock : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unlock)

    //종료 버튼 누르면 종료
        quit_btn.setOnClickListener {
            ActivityCompat.finishAffinity(this)
            finish() //액티비티 종료
        }
    }


//        quit_btn.setOnClickListener {
//            fun onClick(view: View?) {
//                val builder =
//                    AlertDialog.Builder(this@unlock)
//                builder.setMessage("앱을 종료 하시겠습니까?")
//                builder.setTitle("종료 알림창")
//                    .setCancelable(false)
//                    .setPositiveButton(
//                        "네"
//                    ) { dialog, i ->
//                        //                                System.exit(0);
//                        moveTaskToBack(true)
////                        finishAndRemoveTask()
//                        Process.killProcess(Process.myPid())
//                    }
//                    .setNegativeButton(
//                        "아니오"
//                    ) { dialog, i -> dialog.cancel() }
//                val alert = builder.create()
//                alert.setTitle("종료 알림창")
//                alert.show()
//            }
//        }
//    }

}
