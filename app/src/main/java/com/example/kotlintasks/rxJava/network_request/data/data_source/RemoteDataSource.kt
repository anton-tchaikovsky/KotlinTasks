package com.example.kotlintasks.rxJava.network_request.data.data_source

import com.example.kotlintasks.rxJava.network_request.use_case.Fact
import io.reactivex.rxjava3.core.Single

interface RemoteDataSource {
    fun getCatFact(): Single<Fact>
}