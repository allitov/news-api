package com.allitov.newsapi.testcontainer;

import jakarta.annotation.Nonnull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String IMAGE_VERSION = "postgres:12.3";

    private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>(IMAGE_VERSION);

    @Override
    public void initialize(@Nonnull ConfigurableApplicationContext applicationContext) {
        CONTAINER.start();
        TestPropertyValues.of(
                "spring.datasource.url=" + CONTAINER.getJdbcUrl(),
                "spring.datasource.username=" + CONTAINER.getUsername(),
                "spring.datasource.password=" + CONTAINER.getPassword()
        ).applyTo(applicationContext.getEnvironment());
    }
}
