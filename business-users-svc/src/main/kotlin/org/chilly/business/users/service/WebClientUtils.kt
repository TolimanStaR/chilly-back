package org.chilly.business.users.service

import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

inline fun <reified T : Any> WebClient.RequestHeadersSpec<*>.awaitBody(): T = this
    .retrieve()
    .bodyToMono<T>()
    .block()!!

inline fun <reified T : Any> WebClient.RequestHeadersSpec<*>.awaitResult(): Result<T> = runCatching {
    awaitBody<T>()
}.onFailure {
    println("got exception during internal call retrieval for type: ${T::class.qualifiedName}")
    it.printStackTrace()
}