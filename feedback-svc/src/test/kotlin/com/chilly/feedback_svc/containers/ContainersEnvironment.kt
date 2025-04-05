package com.chilly.feedback_svc.containers

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
