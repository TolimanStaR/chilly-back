package org.chilly.business.users.service

import org.chilly.business.users.model.PlaceAddRequest
import org.chilly.business.users.model.RequestStatus
import org.chilly.business.users.repository.PlaceRequestRepository
import org.chilly.business.users.repository.UserRepository
import org.chilly.common.dto.PlaceDto
import org.chilly.common.dto.PlaceRequestDto
import org.chilly.common.exception.AccessDeniedException
import org.chilly.common.exception.CallFailedException
import org.chilly.common.exception.NoSuchEntityException
import org.chilly.common.exception.WrongDataException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.time.Instant
import kotlin.reflect.KMutableProperty

@Service
class RequestService(
    private val userRepository: UserRepository,
    private val repository: PlaceRequestRepository,
    private val webClient: WebClient,
) {

    fun createRequest(userId: Long, request: PlaceDto) {
        val user = findUserOrThrow(userId)

        // check fields of the request
        with(request) {
            val fields = listOf(name, address)
            if (fields.any { it.isNullOrBlank() } || latitude == 0.0 || longitude == 0.0) {
                throw WrongDataException("some required fields are empty")
            }
        }

        val entity = PlaceAddRequest(
            timestamp = Instant.now(),
            status = RequestStatus.CREATED,

            name = request.name,
            address = request.address,
            website = request.website,
            latitude = request.latitude,
            longitude = request.longitude,
            yPage = request.yPage,
            rating = request.rating,
            images = request.images,
            phone = request.phone,
            social = request.social,
            openHours = request.openHours
        ).apply {
            owner = user
            user.requests.add(this)
        }
        repository.save(entity)
    }

    fun getRequests(userId: Long): List<PlaceRequestDto> {
        val user = findUserOrThrow(userId)
        return user.requests
            .sortedByDescending(selector = PlaceAddRequest::timestamp)
            .map { it.toDto() }
    }

    fun changeRequest(userId: Long, requestId: Long, data: PlaceDto) {
        val request = checkExistingRequest(userId, requestId)
        request::name.checkAndSet(data.name)
        request::address.checkAndSet(data.address)
        request::website.checkAndSet(data.website)
        request::yPage.checkAndSet(data.yPage)
        request::phone.checkAndSet(data.phone)
        request::images.checkAndSet(data.images, ::discardListUpdateCheck)
        request::openHours.checkAndSet(data.openHours, ::discardListUpdateCheck)
        request::social.checkAndSet(data.social, ::discardListUpdateCheck)
        request::latitude.checkAndSet(data.latitude)
        request::longitude.checkAndSet(data.longitude)

        repository.save(request)
    }

    fun approveRequest(requestId: Long) {
        val request = repository.findByIdOrNull(requestId)
            ?: throw NoSuchEntityException("cannot find request with id=$requestId")
        checkRequestStatus(request)

        request.status = RequestStatus.APPROVED
        val savedId = callAddPlaceInternal(request.toDto().placeInfo)
            .getOrElse {
                throw CallFailedException("cannot save approved place in the places service, request failed")
            }
        println("saved places with id = $savedId")
    }

    fun declineRequest(requestId: Long, reason: String) {
        val request = repository.findByIdOrNull(requestId)
            ?: throw NoSuchEntityException("cannot find request with id=$requestId")
        checkRequestStatus(request)

        request.status = RequestStatus.DECLINED
        request.reason = reason
    }

    private fun checkRequestStatus(request: PlaceAddRequest) {
        if (request.status != RequestStatus.CREATED) {
            throw AccessDeniedException("request has been already approved or declined")
        }
    }

    private fun PlaceAddRequest.toDto() = PlaceRequestDto(
        /* id = */ id,
        /* ownerId = */ owner.id,
        /* timestamp = */ timestamp.toEpochMilli(),
        /* status = */ status.name,
        /* reason = */ reason,
        /* placeInfo = */ PlaceDto(
            /* id = */ null,
            /* name = */ name,
            /* address = */ address,
            /* website = */ website,
            /* yPage = */ yPage,
            /* rating = */ rating,
            /* images = */ images,
            /* phone = */ phone,
            /* social = */ social,
            /* openHours = */ openHours,
            /* latitude = */ latitude,
            /* longitude = */ longitude,
            /* ownerId = */ owner.id
        )
    )

    private fun findUserOrThrow(id: Long) =
        userRepository.findByIdOrNull(id)
            ?: throw NoSuchEntityException("user with id=$id not found")

    private fun callAddPlaceInternal(place: PlaceDto): Result<Long> =
        webClient.post()
            .uri("http://places-svc/api/places/new/internal")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(place)
            .awaitResult()

    private fun discardListUpdateCheck(old: List<String>?, new: List<String>?): Boolean {
        if (old == new) return true
        return new?.any { it.isBlank() } ?: true
    }

    private inline fun <T> KMutableProperty<T>.checkAndSet(
        newValue: T?,
        guardCond: (T, T) -> Boolean = { a, b -> a == b }
    ) {
        val oldValue = getter.call()
        if (newValue == null || guardCond(oldValue, newValue)) {
            return
        }
        setter.call(newValue)
    }

    fun deleteRequest(userId: Long, requestId: Long) {
        checkExistingRequest(userId, requestId)
        repository.deleteById(requestId)
    }

    private fun checkExistingRequest(userId: Long, requestId: Long): PlaceAddRequest {
        findUserOrThrow(userId)
        val request = repository.findByIdOrNull(requestId)
            ?: throw NoSuchEntityException("no place_request with id=$requestId")

        if (request.owner.id != userId) {
            throw AccessDeniedException("request doesn't belong to user")
        }
        return request
    }
}