package com.miachyn.watcherservice.initializer;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public class DatabaseContainerInitializer {
    @Container
    public static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:11.5"))
            .withUsername("postgres")
            .withPassword("postgres")
            .withDatabaseName("testCurrency")
            .withReuse(true);

    static {
        if (!postgreSQLContainer.isRunning())
            postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void postgreSQLProperties(DynamicPropertyRegistry registry){
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        registry.add("spring.datasource.url",()->jdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }
}
