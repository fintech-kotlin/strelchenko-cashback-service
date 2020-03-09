package ru.tinkoff.fintech

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.kafka.annotation.EnableKafka


@SpringBootApplication
@EnableKafka
class App

fun main(args: Array<String>) {
    SpringApplication.run(App::class.java, *args)
}