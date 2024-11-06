package com.example.kotlintasks.duration

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.time.Duration

class DurationDelegate(
    private val durationCash: DurationCash,
    intervalLogInSeconds: Int,
    duration: Duration = Duration.ZERO
) : ReadWriteProperty<Any?, Duration> {

    init {
        durationCash.saveDuration(duration)
        startLoggerDuration(intervalLogInSeconds)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): Duration =
        durationCash.readDuration()

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Duration) {
        durationCash.saveDuration(value)
    }

    private fun startLoggerDuration(intervalInSeconds: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            do {
                printLogDuration(durationCash.readDuration())
                delay(intervalInSeconds * 1000L)
            } while (true)
        }
    }

    private fun printLogDuration(duration: Duration) {
        duration.toComponents { _, minutes, seconds, nanoseconds ->
            Log.i(
                "Duration",
                "$minutes minutes ${if (nanoseconds < 500_000_000) seconds else (seconds + 1)} seconds"
            )
        }
    }
}