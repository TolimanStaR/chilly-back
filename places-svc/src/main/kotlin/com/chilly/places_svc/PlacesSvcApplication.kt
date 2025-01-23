package com.chilly.places_svc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class PlacesSvcApplication

fun main(args: Array<String>) {
    runApplication<PlacesSvcApplication>(*args)
}