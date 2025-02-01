package com.chilly.places_svc.controller

import com.chilly.places_svc.service.PlaceService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.chilly.common.dto.PlaceDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(PlaceController::class)
class PlaceControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val mapper: ObjectMapper
) {

    @MockkBean
    lateinit var placeServiceMock: PlaceService

    @Test
    fun `all places successfully returns`() {
        every { placeServiceMock.getAllPlaces() } returns listOf(placeDto)

        mockMvc.get("/api/places").andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$[0].id") {
                value(placeDto.id)
            }
        }
    }

    @Test
    fun `save places returns status created`() {
        every { placeServiceMock.savePlaces(any()) } just Runs

        mockMvc.post("/api/places") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(listOf(placeDto))
        }.andExpect {
            status { isCreated() }
        }

        verify { placeServiceMock.savePlaces(eq(listOf(placeDto))) }
    }


    companion object {
        val placeDto = PlaceDto().apply {
            id = 1L
            name = "place"
        }
    }
}