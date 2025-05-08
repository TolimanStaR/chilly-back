package org.chilly.api_gateway.containers;

import org.testcontainers.containers.GenericContainer;

import static java.lang.System.setProperty;

public class EurekaTestContainer extends GenericContainer<EurekaTestContainer> {
    private static final String IMAGE_VERSION = "springcloud/eureka";
    private static final int EUREKA_PORT = 8761;
    private static EurekaTestContainer instance = null;

    private EurekaTestContainer() {
        super(IMAGE_VERSION);
    }

    @Override
    public void start() {
        super.start();
        String eurekaUrl = String.format(
                "http://%s:%d/eureka",
                instance.getHost(),
                instance.getFirstMappedPort()
        );
        setProperty("EUREKA_TEST_URL", eurekaUrl);
    }

    @Override
    public void stop() {}

    static EurekaTestContainer getInstance() {
        if (instance == null) {
            instance = new EurekaTestContainer()
                    .withExposedPorts(EUREKA_PORT);
        }
        return instance;
    }
}
