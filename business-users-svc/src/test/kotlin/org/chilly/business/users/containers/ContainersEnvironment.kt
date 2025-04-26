package org.chilly.business.users.containers

import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
open class ContainersEnvironment {

    companion object {
        @Suppress("UNUSED")
        @Container
        val postgresTestContainer = PostgresTestContainer()
    }
}
