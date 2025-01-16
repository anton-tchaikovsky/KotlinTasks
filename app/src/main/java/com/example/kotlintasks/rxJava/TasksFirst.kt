package com.example.kotlintasks.rxJava

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject

fun main() {

    timer()

    Thread.sleep(100)

    subject()

    Thread.sleep(100)
}

private fun subject() {
    /* Вариант 1*/
    val publishSubject = PublishSubject.create<String>()
    val disposablePublishSubject = publishSubject.subscribe { println("$it from PublishSubject") }
    publishSubject.onNext("1")
    publishSubject.onNext("2")
    publishSubject.onNext("3")

    /* Вариант 2*/
    val replaySubject = ReplaySubject.create<String>()
    replaySubject.onNext("1")
    replaySubject.onNext("2")
    replaySubject.onNext("3")
    val disposableReplaySubject = replaySubject.subscribe { println("$it from ReplaySubject") }
}

private fun timer() {
    // кастомный timer для понимания, в каком потоке работает emitter
    Observable.create { emitter ->
        println("emmit from timer ${Thread.currentThread().name}")
        Thread.sleep(10)
        emitter.onNext(10L)
        emitter.onComplete()
    }
        .subscribeOn(Schedulers.newThread()) // верхний subscribeOn определяет, на каком потоке будет работать emitter и обработка значений снизу (до observeOn())
        .doOnSubscribe {
            println("doOnSubscribeFirst from ${Thread.currentThread().name}") // работает на потоке subscribeOn() снизу
        }
        .doOnNext {
            println("doOnNextFirst from ${Thread.currentThread().name}") // работает на потоке emitter-а (изменить можно с помощью observeOn() сверху)
        }
        .subscribeOn(Schedulers.io()) // т.к. не верхний, то определяет поток только для дополнительных действий onSubscribeFirst
        .map {
            println("map from ${Thread.currentThread().name}") // работает на потоке emitter-а (изменить можно с помощью observeOn() сверху)
            it
        }
        .doOnSubscribe {
            println("doOnSubscribeSecond from ${Thread.currentThread().name}") // работает на потоке subscribeOn() снизу
        }
        .doOnNext {
            println("doOnNextSecond from ${Thread.currentThread().name}") // работает на потоке emitter-а (изменить можно с помощью observeOn() сверху)
        }
        .subscribeOn(Schedulers.computation()) // т.к. не верхний, то определяет поток только для дополнительных действий onSubscribeSecond
        .observeOn(Schedulers.single()) // определяет поток обработки значений снизу
        .doOnSubscribe {
            println("doOnSubscribeThird from ${Thread.currentThread().name}") // работает на исходном потоке (main), т.к. нету снизу subscribeOn()
        }
        .doOnNext {
            println("doOnNextSecondThird from ${Thread.currentThread().name}") // работает на потоке observeOn() сверху
        }
        .flatMap {
            println("flatMap from ${Thread.currentThread().name}")
            Observable.create { emitter ->
                println("emmit from just ${Thread.currentThread().name}")
                emitter.onNext(it)
                emitter.onComplete()
            }
                .subscribeOn(Schedulers.io()) //локальный верхний subscribeOn определяет, на каком потоке будет работать emitter для just и обработка значений снизу
        }
        .subscribe(object : Observer<Long> {
            override fun onSubscribe(d: Disposable) { // подписка всегда из исходного потока (main)
                println("onSubscribe from ${Thread.currentThread().name}")
            }

            override fun onError(e: Throwable) {
                println("onError from ${Thread.currentThread().name}")
            }

            override fun onComplete() {
                println("onComplete from ${Thread.currentThread().name}")
            }

            override fun onNext(t: Long) {
                println("onNext from ${Thread.currentThread().name}")
            }

        })
}