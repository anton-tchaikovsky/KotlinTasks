package com.example.kotlintasks.rxJava.network_request.data.data_source

import com.example.kotlintasks.rxJava.network_request.use_case.Fact
import retrofit2.Call
import retrofit2.http.GET

interface CatFactApi {
    @GET("/fact")
    fun getFact(): Call<Fact>
}