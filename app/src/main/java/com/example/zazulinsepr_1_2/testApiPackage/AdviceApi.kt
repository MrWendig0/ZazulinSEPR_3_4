package com.example.zazulinsepr_1_2.testApiPackage

import retrofit2.Call
import retrofit2.http.GET

interface AdviceApi {
    @GET("advice")
    fun getRandomAdvice(): Call<AdviceResponse>
}
