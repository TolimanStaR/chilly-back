package com.chilly.security_svc.containers;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnvironment {

    @Container
    public static PostgresTestContainer postgresTestContainer = PostgresTestContainer.getInstance();
}
