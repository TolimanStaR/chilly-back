package com.chilly.feedback_svc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class FeedbackSvcApplication

fun main(args: Array<String>) {
    runApplication<FeedbackSvcApplication>(*args)
}