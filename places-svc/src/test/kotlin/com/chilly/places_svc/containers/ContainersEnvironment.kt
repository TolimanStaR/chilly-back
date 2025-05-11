package com.chilly.places_svc.containers;

import org.chilly.common.test.containers.EurekaTestContainer
import org.chilly.common.test.containers.PostgisTestContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
open class ContainersEnvironment {

    companion object {
        @Container
        val postgresTestContainer: PostgisTestContainer = PostgisTestContainer.getInstance()
        @Container
        val eurekaTestContainer: EurekaTestContainer = EurekaTestContainer.getInstance()
    }
}
