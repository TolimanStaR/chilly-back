package com.chilly.places_svc.repository

import com.chilly.places_svc.containers.ContainersEnvironment
import com.chilly.places_svc.model.Place
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
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
        placeRepository.saveAllCheckId(entities)
        entityManager.flush()

        val result = placeRepository.findAllByIdIn(listOf(1L, 3L))
        assertEquals(listOf(entities[0], entities[2]).map { it.id }, result.map{ it.id }, )
    }

    @Test
    fun `findAllByIdIn doesn't throw if not-existing id passed`() {
        val entities = listOf(Place(1L, "name_1"), Place(2L, "name_2"), Place(3L, "name_3"))
        entities.forEach {
            placeRepository.saveCheckId(it)
            entityManager.flush()
        }

        val result = placeRepository.findAllByIdIn(listOf(1L, -10L))
        assertEquals(listOf(entities[0]).map{ it.id }, result.map{ it.id })
    }

    @Test
    fun `places without provided ids are persisted with generated ids`() {
        val entities = listOf(Place(null, "name_1"), Place(null, "name_2"), Place(null, "name_3"))

        val result = placeRepository.saveAllCheckId(entities)

        assert(result.all { it.id != null })
    }
}