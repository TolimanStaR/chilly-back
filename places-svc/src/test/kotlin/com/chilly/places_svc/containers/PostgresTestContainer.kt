package com.chilly.places_svc.containers

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgresTestContainer private constructor() : PostgreSQLContainer<PostgresTestContainer>(IMAGE_VERSION) {

    override fun start() {
        super.start()
        container?.let {
            System.setProperty("DB_URL", it.jdbcUrl)
            System.setProperty("DB_USER", it.username)
            System.setProperty("DB_PASSWORD", it.password)
        }
    }

    override fun stop() = Unit

    companion object {
        private val IMAGE_VERSION = DockerImageName.parse("postgis/postgis:17-3.5-alpine")
            .asCompatibleSubstituteFor("postgres")
        private const val DATABASE_NAME = "test"

        private var container: PostgresTestContainer? = null

        operator fun invoke(): PostgresTestContainer {
            return container ?: PostgresTestContainer()
                .withDatabaseName(DATABASE_NAME)
                .withReuse(true)
                .also { container = it }
        }
    }
}
