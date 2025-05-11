package com.chilly.security_svc.containers;

import org.chilly.common.test.containers.EurekaTestContainer;
import org.chilly.common.test.containers.PostgresTestContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnvironment {
    @Container
    public static PostgresTestContainer postgresTestContainer = PostgresTestContainer.getInstance();
    @Container
    public static EurekaTestContainer eurekaContainer = EurekaTestContainer.getInstance();
}
