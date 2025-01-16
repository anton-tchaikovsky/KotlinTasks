package com.example.kotlintasks.rxJava.network_request.data.data_source

import com.example.kotlintasks.rxJava.network_request.use_case.Fact
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.SingleSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RemoteDataSourceImpl : RemoteDataSource {
    private val service = Retrofit.Builder()
        .baseUrl("https://catfact.ninja")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CatFactApi::class.java)

    private val serviceWithRxJavaAdapter = Retrofit.Builder()
        .baseUrl("https://catfact.ninja")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(CatFactApiWithRxJavaAdapter::class.java)

    override fun getCatFact(): Single<Fact> {
        val single = SingleSubject.create<Fact>()
        service.getFact().enqueue(object : Callback<Fact> {
            override fun onResponse(call: Call<Fact>, response: Response<Fact>) {
                if (response.isSuccessful) {
                    response.body()?.let { single.onSuccess(it) }
                        ?: single.onError(IOException("service error"))
                } else
                    single.onError(IOException("service error"))
            }

            override fun onFailure(call: Call<Fact>, t: Throwable) {
                single.onError(t)
            }
        })
        return single
    }

//    override fun getCatFact(): Single<Fact> =
//        serviceWithRxJavaAdapter.getFact()

}