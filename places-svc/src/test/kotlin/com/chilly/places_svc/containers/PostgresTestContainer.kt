package com.chilly.places_svc.containers;

import org.testcontainers.containers.PostgreSQLContainer;

class PostgresTestContainer private constructor() : PostgreSQLContainer<PostgresTestContainer>(IMAGE_VERSION) {

    override fun start() {
        super.start()
        container?.let {
            System.setProperty("DB_URL", it.jdbcUrl);
            System.setProperty("DB_USER", it.username);
            System.setProperty("DB_PASSWORD", it.password);
        }
    }

    override fun stop() = Unit

    companion object {
        private const val IMAGE_VERSION = "postgres:alpine"
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
