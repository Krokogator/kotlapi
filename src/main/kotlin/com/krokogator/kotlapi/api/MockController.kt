package com.krokogator.kotlapi.api

import kotlinx.coroutines.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class MockController {

    @GetMapping("/blocking")
    fun getMock(): String {
        return heavyTask() + heavyTask()
    }

    @GetMapping("/coroutine")
    suspend fun getCoroutineMock(): String = withContext(Dispatchers.IO){
        val res1 = heavyCoTask()
        val res2 =  heavyCoTask()
        res1 + res2
    }

    @GetMapping("/coroutineasync")
    suspend fun getTest() = withContext(Dispatchers.IO) {
        val res1 = async {
            heavyCoTask()
        }
        val res2 = async {
            heavyCoTask()
        }
        val res3 = async {
            heavyCoTask()
        }
        val res4 = async {
            heavyCoTask()
        }
        val res5 = async {
            heavyCoTask()
        }
        val res6 = async {
            heavyCoTask()
        }
        res1.await() + res2.await() + res3.await() + res4.await() + res5.await() + res6.await()
    }

    private fun heavyTask(): String {
        runBlocking { delay(200L) }
        return "Computed result!"
    }

    private suspend fun heavyCoTask(): String {
        delay(200L)
        return "Computed result!"
    }
}