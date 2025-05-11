package org.chilly.common.test.containers;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 *  Instantiated conatiner sets "DB_URL", "DB_USER", "DB_PASSWORD" environment variables
 *  use provided values in test/resources/application.yml for datasource configuration
 */
public class PostgisTestContainer extends PostgreSQLContainer<PostgisTestContainer> {

    private static final DockerImageName IMAGE_VERSION = DockerImageName.parse("postgis/postgis:17-3.5-alpine")
            .asCompatibleSubstituteFor("postgres");
    private static final String DATABASE_NAME = "test";

    private static PostgisTestContainer container;

    private PostgisTestContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgisTestContainer getInstance() {
        if (container == null) {
            container = new PostgisTestContainer().withDatabaseName(DATABASE_NAME);
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
