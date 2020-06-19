package com.zartre.app.beacondemo

import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // retrofit 사용
        var retrofit = Retrofit.Builder()
            .baseUrl("http://220.67.124.145:8080")  //"http://220.67.124.66:8037" / "http://192.168.0.110:8080" / "http://192.168.0.121:8081" / "http://182.222.126.40:8080"
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var loginservice: forLoginService = retrofit.create(forLoginService::class.java) // retrofit 객체 생성

        login_btn.setOnClickListener{
            var s_id = edit_ID.text.toString()
            var s_pw = edit_PW.text.toString()

            loginservice.requestLogin(s_id, s_pw).enqueue(object: Callback<forLogin> {
                override fun onFailure(call: retrofit2.Call<forLogin>, t: Throwable) {  // 실패할 때
                    Log.e("로그인", t.message)
                    var dialog = AlertDialog.Builder(this@login)
                    dialog.setTitle("ERROR")
                    dialog.setMessage("서버와의 통신이 실패하였습니다.")
                    dialog.show()
                }

                override fun onResponse(call: retrofit2.Call<forLogin>, response: Response<forLogin>) {  // 서버에서 정상 응답이 올 때
                    if (response?.isSuccessful) {
                        var forLogin = response.body()
                        Log.d("LOGIN", "msg : " + forLogin?.msg)
                        Log.d("LOGIN", "code : " + forLogin?.code)
                        Log.d("LOGIN", "allowed_area : " + forLogin?.allowed_area)
                        Toast.makeText(this@login, "로그인에 성공하였습니다. \n 즐거운 하루 되세요.\n", Toast.LENGTH_SHORT).show()

                        var intent = Intent(applicationContext, MainActivity::class.java).apply {
                            putExtra("allowed_area", forLogin?.allowed_area)
                        }
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this@login, "로그인에 실패했습니다. \n 아이디 혹은 비밀번호를 확인하세요.", Toast.LENGTH_LONG).show()
                    }
                }
            })


        }
        signUp_btn.setOnClickListener {
            var intent = Intent(applicationContext, signup::class.java)
            startActivity(intent)
        }
    }
}

