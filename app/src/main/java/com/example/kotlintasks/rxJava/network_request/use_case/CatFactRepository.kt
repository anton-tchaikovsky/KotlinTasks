package com.example.kotlintasks.rxJava.network_request.use_case

import io.reactivex.rxjava3.core.Single

interface CatFactRepository {
    fun getFact(): Single<Fact>
}