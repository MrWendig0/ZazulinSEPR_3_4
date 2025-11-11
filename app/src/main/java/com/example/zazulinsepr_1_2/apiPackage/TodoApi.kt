package com.example.zazulinsepr_1_2.apiPackage

import retrofit2.Call
import retrofit2.http.GET

interface TodoApi {
    @GET("todos")
    fun getTodos(): Call<List<Todo>>
}
