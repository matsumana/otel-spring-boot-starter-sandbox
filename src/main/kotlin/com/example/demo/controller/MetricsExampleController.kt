package com.example.demo.controller

import com.example.demo.service.MetricsExampleService
import com.example.demo.util.MetricsUtil.measureDuration
import com.example.demo.util.RandomUtil.randomValue
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tags
import io.micrometer.core.instrument.Timer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.util.concurrent.atomic.AtomicLong
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toJavaDuration

@RestController
@RequestMapping("/example")
class MetricsExampleController(
    private val registry: MeterRegistry,
    private val metricsExampleService: MetricsExampleService,
) {
    private val counter = Counter.builder("example.counter")
        .description("An example counter")
        .tags(
            Tags.of(
                "tag1", "aaa",
                "tag2", "bbb",
            )
        )
        .register(registry)

    private val gaugeValue = AtomicLong(0)

    private val distributionSummary = DistributionSummary.builder("example.distributionsummary")
        .description("An example DistributionSummary")
        .tags(
            Tags.of(
                "tag.aaa", "foo",
                "tag.bbb", "bar",
            )
        )
        .minimumExpectedValue(0.1)
        .maximumExpectedValue(1_000.0)
        .publishPercentileHistogram()
        .register(registry)

    private val timer = Timer.builder("example.timer")
        .description("An example timer")
        .tags(
            Tags.of(
                "tag.ccc", "baz",
                "tag.ddd", "qux",
            )
        )
        .minimumExpectedValue(Duration.ofMillis(100))
        .maximumExpectedValue(Duration.ofMinutes(3))
        .publishPercentileHistogram()
        .register(registry)

    init {
        Gauge.builder("example.gauge", gaugeValue) { it.get().toDouble() }
            .tags(
                Tags.of(
                    "tag.eee", "quux",
                    "tag.fff", "corge",
                )
            )
            .register(registry)
    }

    @GetMapping("/metrics")
    fun index(): String {
        updateMetrics()
        metricsExampleService.doSomething()
        return "ok"
    }

    private fun updateMetrics() {
        counter.increment()

        gaugeValue.set(randomValue())

        distributionSummary.record(randomValue().toDouble())

        measureDuration(timer) {
            doSomething()
        }
    }

    private fun doSomething() {
        // Simulate a process
        Thread.sleep(
            randomValue()
                .toDuration(DurationUnit.MILLISECONDS)
                .toJavaDuration()
        )
    }
}
