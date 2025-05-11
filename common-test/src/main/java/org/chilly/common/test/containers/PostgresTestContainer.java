package org.chilly.common.test.containers;

import org.testcontainers.containers.PostgreSQLContainer;

/**
 *  Instantiated conatiner sets "DB_URL", "DB_USER", "DB_PASSWORD" environment variables
 *  use provided values in test/resources/application.yml for datasource configuration
 */
public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    private static final String IMAGE_VERSION = "postgres:alpine";
    private static final String DATABASE_NAME = "test";

    private static PostgresTestContainer container;

    private PostgresTestContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgresTestContainer getInstance() {
        if (container == null) {
            container = new PostgresTestContainer().withDatabaseName(DATABASE_NAME);
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USER", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {}
}
