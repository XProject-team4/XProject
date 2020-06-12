package com.example.smartkey_app

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface forLoginService {
    @FormUrlEncoded
    @POST("/client_data/login")
    fun requestLogin(
        @Field("identification") s_id:String,
        @Field("password") s_pw:String
    ) : Call<forLogin>
}