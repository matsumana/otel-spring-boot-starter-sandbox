package com.example.demo.service

import com.example.demo.util.RandomUtil.randomValue
import io.micrometer.core.annotation.Timed
import org.springframework.stereotype.Service
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toJavaDuration

@Service
class MetricsExampleService {

    @Timed(
        value = "example.timed",
        description = "An example timer with Timed annotation",
        histogram = true
    )
    fun doSomething() {
        // Simulate a process
        Thread.sleep(
            randomValue()
                .toDuration(DurationUnit.MILLISECONDS)
                .toJavaDuration()
        )
    }
}
