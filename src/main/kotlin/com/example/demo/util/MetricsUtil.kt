package com.example.demo.util

import io.micrometer.core.instrument.Timer
import kotlin.time.measureTimedValue
import kotlin.time.toJavaDuration

object MetricsUtil {

    fun <T> measureDuration(timer: Timer, block: () -> T): T {
        val (value, duration) = measureTimedValue(block)
        timer.record(duration.toJavaDuration())
        return value
    }
}
