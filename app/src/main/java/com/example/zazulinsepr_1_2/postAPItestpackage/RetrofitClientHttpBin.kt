package com.example.zazulinsepr_1_2.postAPItestpackage

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientHttpBin {
    private const val BASE_URL = "https://httpbin.org/"

    val api: HttpBinApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HttpBinApi::class.java)
    }
}