package com.example.kotlintasks.rxJava.network_request.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlintasks.rxJava.network_request.use_case.CatFactRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CatFactViewModel (private val repository: CatFactRepository): ViewModel() {

    private var disposable: Disposable? = null

    private val _liveData = MutableLiveData<CatFactViewState>()

    val liveData: LiveData<CatFactViewState>
        get() = _liveData

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

    fun getCatFact(){
        subscribeForRepository()
    }

    private fun subscribeForRepository() {
        disposable = repository.getFact()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { _liveData.value = CatFactViewState.Loading }
            .map { it.fact }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { fact ->
                    _liveData.value = CatFactViewState.Success(fact)
                },
                { error ->
                    _liveData.value = error.message.let { CatFactViewState.Error(it) }
                }
            )
    }

}