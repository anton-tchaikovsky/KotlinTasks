package com.example.kotlintasks.rxJava.timer

import io.reactivex.rxjava3.core.Observable
import kotlin.time.Duration

interface Timer {
    fun getTimer(): Observable<Duration>
}