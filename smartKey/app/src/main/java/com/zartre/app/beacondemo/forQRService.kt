package com.zartre.app.beacondemo

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface forQRService {
    @FormUrlEncoded
    @POST("/client_data/first_qr_scan")
    fun requestQRScan(
        @Field("store") store:String,
        @Field("allowed_area") area:String
    ) : Call<forQR>
}