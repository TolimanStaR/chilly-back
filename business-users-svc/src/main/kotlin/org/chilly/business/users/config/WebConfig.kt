package org.chilly.business.users.config

import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebConfig {

    @Bean
    @LoadBalanced
    fun webClientBuilder(): WebClient.Builder = WebClient.builder()

    @Bean
    fun webClient(builder: WebClient.Builder): WebClient = builder.build()

}