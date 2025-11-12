package com.example.zazulinsepr_1_2.postAPItestpackage

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface HttpBinApi {
    @POST("post")
    fun sendData(@Body user: UserRequest): Call<HttpBinResponse>
}