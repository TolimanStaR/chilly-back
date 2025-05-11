package org.chilly.common.test.containers;

import org.testcontainers.containers.MinIOContainer;

/**
 *  Instantiated conatiner sets "MINIO_ENDPOINT", "MINIO_SECRET_KEY", "MINIO_ACCESS_KEY" environment variables
 *  use provided values in test/resources/application.yml for minio configuration
 */
public class MinioTestContainer extends MinIOContainer {

    private static final String IMAGE_VERSION = "minio/minio:latest";

    private static MinioTestContainer container;

    private MinioTestContainer() {
        super(IMAGE_VERSION);
    }

    public static MinioTestContainer getInstance() {
        if (container == null) {
            container = new MinioTestContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("MINIO_ENDPOINT", getS3URL());
        System.setProperty("MINIO_ACCESS_KEY", getUserName());
        System.setProperty("MINIO_SECRET_KEY", getPassword());
    }

    @Override
    public void stop() {}
}
