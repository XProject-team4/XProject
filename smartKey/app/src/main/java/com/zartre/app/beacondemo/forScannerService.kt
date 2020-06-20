package com.zartre.app.beacondemo

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface forScannerService {
    @FormUrlEncoded
    @POST("/client_data/door_open")
    fun requestUuid(
        @Field("id") id: String,
        @Field("uuid") uuid: String
    ) : Call<forScanner>
}