package com.example.smartkey_app

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface forSignupService {
    @FormUrlEncoded
//    @Headers("accept: application/json", "content-type: application/json")
    @POST("/client_data/")
    fun requestSignup(
        @Field("identification") s_id:String,
        @Field("password") s_pw:String,
        @Field("name") s_name:String,
        @Field("phone_number") s_phoneNum:String

    ) : Call<forSignup>

}