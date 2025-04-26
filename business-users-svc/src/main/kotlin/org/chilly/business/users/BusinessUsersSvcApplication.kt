package org.chilly.business.users

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class BusinessUsersSvcApplication

fun main(args: Array<String>) {
    runApplication<BusinessUsersSvcApplication>(*args)
}