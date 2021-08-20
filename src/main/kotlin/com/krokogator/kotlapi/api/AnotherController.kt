package com.krokogator.kotlapi.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class AnotherController {

    @GetMapping("/")
    suspend fun find(): String = withContext(Dispatchers.IO){
        val start = System.currentTimeMillis()
        println(Thread.currentThread().name + "   starting at " + start)
        val product = async {
            findById()
        }
        val product2 = async {
            findById2()
        }
        println(Thread.currentThread().name + "   ending  " + (System.currentTimeMillis() - start))
        product.await() + product2.await()
    }

    @GetMapping("/v2")
    suspend fun findv2(): String {
        var result = ""
        coroutineScope {
            val start = System.currentTimeMillis()
            println(Thread.currentThread().name + "   starting at " + start)
            val product = async {
                findById()
            }
            val product2 = async {
                findById2()
            }
            result = product.await() + product2.await()
            println(Thread.currentThread().name + "   ending  " + (System.currentTimeMillis() - start))
        }
        return result
    }


    suspend fun findById(): String {
        println(Thread.currentThread().name + "   starting find 1")
        val restTemplate = RestTemplate()
        val response = restTemplate.getForObject("https://httpbin.org/ip", String::class.java)
        Thread.sleep(2000)
        return response!!
    }

    suspend fun findById2(): String {
        println(Thread.currentThread().name + "   starting find 2")
        val restTemplate = RestTemplate()
        val response = restTemplate.getForObject("https://httpbin.org/ip", String::class.java)
        Thread.sleep(2000)
        return response!!
    }
}