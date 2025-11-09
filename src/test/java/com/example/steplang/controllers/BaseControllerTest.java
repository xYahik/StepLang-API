package com.example.steplang.controllers;

import com.example.steplang.PostgresContainer;
import com.example.steplang.TestUserCreation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public abstract class BaseControllerTest {

    @DynamicPropertySource
    public static void configureTestDatabase(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", PostgresContainer.getInstance()::getJdbcUrl);
        registry.add("spring.datasource.username",PostgresContainer.getInstance()::getUsername);
        registry.add("spring.datasource.password", PostgresContainer.getInstance()::getPassword);
    }

    @Autowired
    public MockMvc mockMvc;
    public static String accessToken;

    @BeforeEach
    void setUp() throws Exception{
        TestUserCreation.clearToken();
        TestUserCreation.init(mockMvc);
    }

}
