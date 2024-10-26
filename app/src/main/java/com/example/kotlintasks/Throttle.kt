package com.example.kotlintasks

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform


// выдает первый элемент и запускает таймер, все элементы до остановки таймера игнорируются
fun <T> Flow<T>.throttleFirst(periodMilliseconds: Long): Flow<T> {
    require(periodMilliseconds > 0) { "Incorrect period" }
    return flow {
        var lastTime = 0L
        this@throttleFirst.collect {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTime >= periodMilliseconds) {
                lastTime = currentTime
                emit(it)
            }
        }
    }
}

// выдает первый элемент и запускает таймер, затем в каждом интервале таймера выдает последнее значение
fun <T> Flow<T>.throttleLatest(periodMilliseconds: Long): Flow<T> {
    require(periodMilliseconds > 0) { "Incorrect period" }
    return this@throttleLatest
        .conflate()
        .transform {
            emit(it)
            delay(periodMilliseconds)
        }
}

