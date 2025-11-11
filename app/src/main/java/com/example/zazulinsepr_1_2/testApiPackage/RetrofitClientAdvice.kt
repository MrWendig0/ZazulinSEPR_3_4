package com.example.zazulinsepr_1_2.testApiPackage

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientAdvice {
    private const val BASE_URL = "https://api.adviceslip.com/"

    val adviceApi: AdviceApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AdviceApi::class.java)
    }
}
