package org.chilly.business.users.service

import org.chilly.business.users.model.PlaceAddRequest
import org.chilly.business.users.model.RequestStatus
import org.chilly.business.users.repository.PlaceRequestRepository
import org.chilly.business.users.repository.UserRepository
import org.chilly.common.dto.PlaceDto
import org.chilly.common.dto.PlaceRequestDto
import org.chilly.common.exception.AccessDeniedException
import org.chilly.common.exception.NoSuchEntityException
import org.chilly.common.exception.WrongDataException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Instant
import kotlin.reflect.KMutableProperty

@Service
class RequestService(
    private val userRepository: UserRepository,
    private val repository: PlaceRequestRepository
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
        }
        repository.save(entity)
    }

    fun getRequests(userId: Long): List<PlaceRequestDto> {
        val user = findUserOrThrow(userId)
        return user.requests
            .sortedByDescending(selector = PlaceAddRequest::timestamp)
            .map { it.toDto() }
    }

    private fun PlaceAddRequest.toDto() = PlaceRequestDto(
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
            /* longitude = */ longitude
        )
    )

    private fun findUserOrThrow(id: Long) =
        userRepository.findByIdOrNull(id)
            ?: throw NoSuchEntityException("user with id=$id not found")

    fun changeRequest(userId: Long, requestId: Long, data: PlaceDto) {
        val request = checkExistingRequest(userId, requestId)
        request::name.checkAndSet(data.name)
        request::address.checkAndSet(data.address)
        request::website.checkAndSet(data.website)
        request::yPage.checkAndSet(data.yPage)
        request::phone.checkAndSet(data.phone)
        request::images.checkAndSet(data.images, ::checkLists)
        request::openHours.checkAndSet(data.openHours, ::checkLists)
        request::social.checkAndSet(data.social, ::checkLists)
        request::latitude.checkAndSet(data.latitude)
        request::longitude.checkAndSet(data.longitude)

        repository.save(request)
    }

    private fun checkLists(old: List<String>?, new: List<String>?): Boolean {
        if (old == new) return false
        return new?.all { it.isNotBlank() } ?: false
    }

    private inline fun <T> KMutableProperty<T>.checkAndSet(
        newValue: T?,
        check: (T, T) -> Boolean = { a, b -> a == b }
    ) {
        val oldValue = getter.call()
        if (newValue == null || check(oldValue, newValue)) {
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