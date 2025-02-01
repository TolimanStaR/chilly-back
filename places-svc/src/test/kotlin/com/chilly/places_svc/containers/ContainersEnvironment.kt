package com.chilly.places_svc.containers;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
open class ContainersEnvironment {

    companion object {
        @Container
        val postgresTestContainer = PostgresTestContainer()
    }
}
