package com.krokogator.kotlapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class KotlapiApplication

fun main(args: Array<String>) {
	runApplication<KotlapiApplication>(*args)
}
