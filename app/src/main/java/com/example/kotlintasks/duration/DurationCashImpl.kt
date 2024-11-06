package com.example.kotlintasks.duration

import kotlin.properties.Delegates
import kotlin.time.Duration

class DurationCashImpl: DurationCash {
    private var duration: Duration by Delegates.notNull()

    override fun saveDuration(duration: Duration) {
        this.duration = duration
    }

    override fun readDuration(): Duration = duration
}