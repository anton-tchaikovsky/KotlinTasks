package com.example.kotlintasks.rxJava.timer

import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TimerImpl: Timer {
    override fun getTimer(): Observable<Duration> =
        Observable.interval(1, TimeUnit.SECONDS)
            .map { it.toDuration(DurationUnit.SECONDS) }
}