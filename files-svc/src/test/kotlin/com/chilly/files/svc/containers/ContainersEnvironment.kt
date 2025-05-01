package com.chilly.files.svc.containers

import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
open class ContainersEnvironment {

    companion object {
        @Container
        val minIO = MinIOTestContainer()
    }
}
