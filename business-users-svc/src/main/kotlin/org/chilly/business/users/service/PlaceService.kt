package org.chilly.business.users.service

import org.chilly.common.dto.PlaceDto
import org.chilly.common.exception.CallFailedException
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class PlaceService(
    private val webClient: WebClient
) {
    fun getOwnedPlaces(userId: Long): List<PlaceDto> {
        return callInternalOwnedPlaces(userId)
            .getOrElse {
                throw CallFailedException("couldn't get owned places, internal call failed")
            }
    }

    private fun callInternalOwnedPlaces(userId: Long): Result<List<PlaceDto>> =
        webClient.get()
            .uri("http://places-svc/api/places/owned_by/$userId/internal")
            .awaitResult()

}
