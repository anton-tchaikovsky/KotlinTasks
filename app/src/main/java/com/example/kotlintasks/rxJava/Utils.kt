package com.example.kotlintasks.rxJava

import kotlin.time.Duration

fun Duration.formatToString(): String =
    this.toComponents { hours, minutes, seconds, _ ->
        if (hours == 0L)
            "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
        else
            "$hours:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }