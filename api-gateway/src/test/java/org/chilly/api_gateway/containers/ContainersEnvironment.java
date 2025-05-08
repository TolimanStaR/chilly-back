package org.chilly.api_gateway.containers;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnvironment {
    @Container
    public static EurekaTestContainer eurekaTestContainer = EurekaTestContainer.getInstance();
}
