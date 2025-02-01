package com.chilly.places_svc.repository

import com.chilly.places_svc.containers.ContainersEnvironment
import com.chilly.places_svc.model.Place
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE
import kotlin.test.assertEquals

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class PlaceRepositoryTest(
    @Autowired val entityManager: TestEntityManager,
    @Autowired val placeRepository: PlaceRepository
) : ContainersEnvironment() {

    @AfterEach
    fun tearDown() {
        placeRepository.deleteAll()
    }

    @Test
    fun `findAllByIdIn returns correct entities`() {
        val entities = listOf(Place(1L, "name_1"), Place(2L, "name_2"), Place(3L, "name_3"))
        with(entityManager) {
            entities.forEach { persist(it) }
            flush()
        }

        val result = placeRepository.findAllByIdIn(listOf(1L, 3L))
        assertEquals(result, listOf(entities[0], entities[2]))
    }

    @Test
    fun `findAllByIdIn doesn't throw if not-existing id passed`() {
        val entities = listOf(Place(1L, "name_1"), Place(2L, "name_2"), Place(3L, "name_3"))
        with(entityManager) {
            entities.forEach { persist(it) }
            flush()
        }

        val result = placeRepository.findAllByIdIn(listOf(1L, -10L))
        assertEquals(result, listOf(entities[0]))
    }
}