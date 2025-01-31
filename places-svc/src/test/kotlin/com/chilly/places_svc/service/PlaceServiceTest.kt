package com.chilly.places_svc.service

import com.chilly.places_svc.mapper.PlaceMapper
import com.chilly.places_svc.model.Place
import com.chilly.places_svc.repository.PlaceRepository
import io.mockk.*
import org.chilly.common.dto.PlaceDto
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PlaceServiceTest {

    private val mapperMock: PlaceMapper = mockk()
    private val repoMock: PlaceRepository = mockk()

    private val underTest = PlaceService(mapperMock, repoMock)

    @AfterEach
    fun tearDown() {
        clearMocks(mapperMock, repoMock)
    }

    @Test
    fun `savePlaces removes previous places and saves new places`() {
        every { mapperMock.toEntity(any()) } returns Entity
        every { repoMock.saveAll<Place>(any()) } returns emptyList()
        every { repoMock.deleteAll() } just Runs

        underTest.savePlaces(listOf(Dto))

        verify { repoMock.deleteAll() }
        verify { repoMock.saveAll<Place>(any()) }
    }

    @Test
    fun `get places uses repo findAll`() {
        every { mapperMock.toDto(any()) } returns Dto
        every { repoMock.findAll() } returns listOf(Entity)

        val result = underTest.getAllPlaces()

        assertEquals(result, listOf(Dto))
        verify { repoMock.findAll() }
    }

    @Test
    fun `get places for id uses findAllByIdIn`() {
        every { mapperMock.toDto(any()) } returns Dto
        every { repoMock.findAllByIdIn(any()) } returns listOf(Entity)

        val result = underTest.getPlacesByIds(listOf(1))

        assertEquals(result, listOf(Dto))
        verify { repoMock.findAllByIdIn(any()) }
    }

    companion object {
        val Entity = Place(1L, "name")
        val Dto = PlaceDto()
    }
}