package com.example.kotlintasks.duration

import kotlin.time.Duration

interface DurationCash {
    fun saveDuration(duration: Duration)

    fun readDuration(): Duration
}