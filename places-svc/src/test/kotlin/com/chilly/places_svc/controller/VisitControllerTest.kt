package com.chilly.places_svc.controller

import com.chilly.places_svc.service.VisitService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.chilly.common.dto.SaveVisitRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.util.*

@WebMvcTest(VisitController::class)
class VisitControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val mapper: ObjectMapper
) {

    @MockkBean
    lateinit var visitServiceMock: VisitService

    @Test
    fun `save visit produces correct status`() {
        val request = SaveVisitRequest(1L, Date())
        val userId = 101L
        every { visitServiceMock.saveVisit(any(), any()) } just Runs

        mockMvc.post("/api/visits") {
            content = mapper.writeValueAsString(request)
            contentType = MediaType.APPLICATION_JSON
            headers {
                header("UserId", userId)
            }
        }.andExpect {
            status { isOk() }
        }

        verify { visitServiceMock.saveVisit(eq(userId), eq(request)) }
    }

}
