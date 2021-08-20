package com.krokogator.kotlapi.api

import kotlinx.coroutines.*
import kotlinx.coroutines.reactor.mono
import org.apache.logging.log4j.util.Strings
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.function.Consumer
import kotlin.coroutines.suspendCoroutine

@RestController
@RequestMapping("/api")
class MockController {

    @GetMapping("/blocking")
    fun getMock(): String {
        return heavyTask() + heavyTask()
    }

    @GetMapping("/mono")
    fun getMockMono(): Mono<String> {
        return heavyTaskMono()
            .flatMap{r1 -> heavyTaskMono()
            .map { r2 -> r1 + r2 }
        }
    }

    @GetMapping("/coroutine")
    suspend fun getCoroutineMock(): String {
        return heavyCoTask() + heavyCoTask()
    }

    @GetMapping("/coroutineasync")
    suspend fun getTest() = withContext(Dispatchers.IO) {
        val res1 = async {
            heavyCoTask()
        }
        val res2 = async {
            heavyCoTask()
        }
        res1.await() + res2.await()
    }

    @GetMapping("/monoasync")
    fun getMonoAsync() : Mono<String> {
        return Mono.zip(heavyTaskMono(), heavyTaskMono()) { s1, s2 -> s1 + s2 }
    }

    private fun heavyTask(): String {
        println("async : " + Thread.currentThread().name)
        Thread.sleep(50)
        println("async after delay: " + Thread.currentThread().name)
        return "Computed result!"
    }

    private fun heavyTaskMono(): Mono<String> {
        return Mono.just("Computed result!")
            .doOnNext{ println("async : " + Thread.currentThread().name) }
            .delayElement(Duration.ofMillis(50))
            .doOnNext{ println("async  after delay: " + Thread.currentThread().name) }
    }

    private suspend fun heavyCoTask(): String {
        println("async : " + Thread.currentThread().name)
        delay(50)
        println("async  after delay: " + Thread.currentThread().name)
        return "Computed result!"
    }
}