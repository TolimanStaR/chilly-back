package com.chilly.places_svc.service

import com.chilly.places_svc.mapper.VisitMapper
import com.chilly.places_svc.model.Visit
import com.chilly.places_svc.repository.PlaceRepository
import com.chilly.places_svc.repository.VisitRepository
import io.mockk.*
import org.chilly.common.dto.SaveVisitRequest
import org.chilly.common.exception.NoSuchEntityException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import java.util.*

class VisitServiceTest {

    private val mapperMock: VisitMapper = mockk()
    private val visitRepoMock: VisitRepository = mockk()
    private val placeRepoMock: PlaceRepository = mockk()

    private val underTest = VisitService(
        visitRepoMock, mapperMock, placeRepoMock
    )

    @AfterEach
    fun tearDown() {
        clearMocks(visitRepoMock, mapperMock, placeRepoMock)
    }

    @Test
    fun `when no place correct exception thrown`() {
        every { placeRepoMock.findByIdOrNull(any()) } returns null

        assertThrows<NoSuchEntityException> {
            underTest.saveVisit(-1L , SaveVisitRequest(1L, Date()))
        }
    }

    @Test
    fun `when no visit correct exception thrown`() {
        every { visitRepoMock.findByIdOrNull(any()) } returns null

        assertThrows<NoSuchEntityException> {
            underTest.deleteVisit(-1L, -1L)
        }
    }

    @Test
    fun `can delete visit`() {
        every { visitRepoMock.findByIdOrNull(eq(1L)) } returns visit
        every { visitRepoMock.deleteById(any()) } just Runs

        underTest.deleteVisit(1L, 1L)

        verify { visitRepoMock.deleteById(eq(1L)) }
    }

    companion object {
        val visit = Visit(Date()).apply { id = 1L; userId = 1L }
    }
}