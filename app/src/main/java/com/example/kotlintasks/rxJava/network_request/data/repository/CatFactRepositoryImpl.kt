package com.example.kotlintasks.rxJava.network_request.data.repository

import com.example.kotlintasks.rxJava.network_request.data.data_source.RemoteDataSource
import com.example.kotlintasks.rxJava.network_request.use_case.CatFactRepository
import com.example.kotlintasks.rxJava.network_request.use_case.Fact
import io.reactivex.rxjava3.core.Single

class CatFactRepositoryImpl(private val remoteDataSource: RemoteDataSource) : CatFactRepository {
    override fun getFact(): Single<Fact> =
        remoteDataSource.getCatFact()
}