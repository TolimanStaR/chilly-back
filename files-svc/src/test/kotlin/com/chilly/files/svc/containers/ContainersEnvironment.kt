package com.chilly.files.svc.containers

import org.chilly.common.test.containers.EurekaTestContainer
import org.chilly.common.test.containers.MinioTestContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
open class ContainersEnvironment {

    companion object {
        @Container
        val minIO: MinioTestContainer = MinioTestContainer.getInstance()
        @Container
        val eurekaTestContainer: EurekaTestContainer = EurekaTestContainer.getInstance()
    }
}
