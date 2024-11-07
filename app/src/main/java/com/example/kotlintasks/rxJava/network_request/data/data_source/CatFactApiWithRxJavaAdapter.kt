package com.example.kotlintasks.rxJava.network_request.data.data_source

import com.example.kotlintasks.rxJava.network_request.use_case.Fact
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface CatFactApiWithRxJavaAdapter {
    @GET("/fact")
    fun getFact(): Single<Fact>
}