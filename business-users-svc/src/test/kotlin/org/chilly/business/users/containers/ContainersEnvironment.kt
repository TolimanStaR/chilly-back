package org.chilly.business.users.containers

import org.chilly.common.test.containers.EurekaTestContainer
import org.chilly.common.test.containers.PostgresTestContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
open class ContainersEnvironment {

    companion object {
        @Container
        val postgresTestContainer: PostgresTestContainer = PostgresTestContainer.getInstance()
        @Container
        val eurekaTestContainer: EurekaTestContainer = EurekaTestContainer.getInstance()
    }
}
