package com.example.smartkey_app

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class signup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // retrofit 사용
        var retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.110:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var signupservice: forSignupService = retrofit.create(forSignupService::class.java)


        signUp_btn.setOnClickListener{
            var s_id = edit_id_signup.text.toString()
            var s_pw = edit_pw_signup.text.toString()
            var s_name = edit_name_signup.text.toString()
            var s_phoneNum = edit_phoneNum_signup.text.toString()

//            System.out.println(s_id + s_pw + s_name + s_phoneNum)
            
            signupservice.requestSignup(s_id, s_pw, s_name, s_phoneNum).enqueue(object : Callback<forSignup> {  // 회원가입 할 때 enqueue로 비동기 쓰레딩
                override fun onFailure(call: Call<forSignup>, t: Throwable) {  // 실패할 때
                    Log.e("회원가입", t.message)
                    var dialog = AlertDialog.Builder(this@signup)
                    dialog.setTitle("Error")
                    dialog.setMessage("서버와의 통신이 실패하였습니다.")
                    dialog.show()
                }

                override fun onResponse(call: Call<forSignup>, response: Response<forSignup>) {  // 서버에서 정상 응답이 올 때
                    if(response?.isSuccessful) {  // 회원가입이 정상적으로 될 때
                        var forSignup = response.body()
                        Log.d("SIGNUP", "msg : " + forSignup?.msg)
                        Log.d("SIGNUP", "code : " + forSignup?.code)
                        var dialog = AlertDialog.Builder(this@signup)
                        Toast.makeText(this@signup, "회원가입이 완료되었습니다. \n 아이디 : " + s_id, Toast.LENGTH_SHORT).show()
                        intent.putExtra("identification", s_id)
                        startActivity(intent)
                        finish()
                    }
                    else {  // 회원가입에 실패 할 때
                        Toast.makeText(getApplicationContext(), "아이디가 중복되었습니다. \n 새로운 아이디를 입력하세요.", Toast.LENGTH_LONG).show()
                    }
                }
                
            })
        }







    }
}
