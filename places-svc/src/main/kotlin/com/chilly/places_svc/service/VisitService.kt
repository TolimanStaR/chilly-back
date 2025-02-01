package com.chilly.places_svc.service

import com.chilly.places_svc.mapper.VisitMapper
import com.chilly.places_svc.model.Visit
import com.chilly.places_svc.repository.PlaceRepository
import com.chilly.places_svc.repository.VisitRepository
import org.chilly.common.dto.SaveVisitRequest
import org.chilly.common.exception.AccessDeniedException
import org.chilly.common.exception.NoSuchEntityException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class VisitService(
    private val visitRepository: VisitRepository,
    private val visitMapper: VisitMapper,
    private val placeRepository: PlaceRepository,
) {

    fun saveVisit(userId: Long, request: SaveVisitRequest) {
        val place = findPlaceOrException(request.placeId)
        val date = request.timestamp ?: Date()

        val visit = Visit(date).apply {
            this.place = place
            this.userId = userId
        }

        visitRepository.save(visit)
    }

    fun getVisited(userId: Long) = visitRepository.findAllByUserId(userId)
        .map(visitMapper::toDto)

    fun deleteVisit(userId: Long, visitId: Long) {
        val visit = findVisitOrException(visitId)

        if (visit.userId != userId) {
            throw AccessDeniedException("cannot remove visit from another user")
        }

        visitRepository.deleteById(visitId)
    }

    private fun findPlaceOrException(id: Long) = placeRepository.findByIdOrNull(id)
        ?: throw NoSuchEntityException("No place with id=$id")

    private fun findVisitOrException(id: Long) = visitRepository.findByIdOrNull(id)
        ?: throw NoSuchEntityException("No visit with id=$id")
}