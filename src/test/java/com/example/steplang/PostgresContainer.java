package com.example.steplang;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainer {
    private static final PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    static {
        container.start();
    }

    public static PostgreSQLContainer<?> getInstance() {
        return container;
    }
}
