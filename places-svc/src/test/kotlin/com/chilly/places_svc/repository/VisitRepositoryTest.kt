package com.chilly.places_svc.repository

import com.chilly.places_svc.containers.ContainersEnvironment
import com.chilly.places_svc.model.Place
import com.chilly.places_svc.model.Visit
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE
import java.util.*
import kotlin.test.assertEquals

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class VisitRepositoryTest(
    @Autowired val entityManager: TestEntityManager,
    @Autowired val visitRepository: VisitRepository
) : ContainersEnvironment() {


    @AfterEach
    fun tearDown() {
        visitRepository.deleteAll()
    }

    @Test
    fun `findAllByIdIn returns correct entities`() {
        val place = Place(1L, "name")

        val entities = listOf(
            Visit(Date()).apply { userId = 20; this.place = place },
            Visit(Date()).apply { userId = 10; this.place = place },
            Visit(Date()).apply { userId = 20; this.place = place }
        )
        with(entityManager) {
            persist(place)
            entities.forEach { persist(it) }
            flush()
        }

        val result = visitRepository.findAllByUserId(20L)
        result.forEach { assertEquals(20L, it.userId) }
        assertEquals(result.map(Visit::id), listOf(entities[0].id, entities[2].id))
    }
}