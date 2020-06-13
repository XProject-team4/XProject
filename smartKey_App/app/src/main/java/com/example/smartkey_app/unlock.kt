package com.example.smartkey_app

import android.os.Bundle
import android.os.Process
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_unlock.*

class unlock : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unlock)

        quit_btn.setOnClickListener {
            fun onClick(view: View?) {
                val builder =
                    AlertDialog.Builder(this@unlock)
                builder.setMessage("앱을 종료 하시겠습니까?")
                builder.setTitle("종료 알림창")
                    .setCancelable(false)
                    .setPositiveButton(
                        "네"
                    ) { dialog, i ->
                        //                                System.exit(0);
                        moveTaskToBack(true)
//                        finishAndRemoveTask()
                        Process.killProcess(Process.myPid())
                    }
                    .setNegativeButton(
                        "아니오"
                    ) { dialog, i -> dialog.cancel() }
                val alert = builder.create()
                alert.setTitle("종료 알림창")
                alert.show()
            }
        }
    }
}