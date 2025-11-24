package com.example.steplang.controllers;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest extends BaseControllerTest {

    @Test
    @Order(1)
    public void registerUser_ReturnOk() throws Exception {
        String body = """
                {
                    "email":"testregister@gmail.com",
                    "password":"testregisterpassword"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    public void loginUser_ReturnToken() throws Exception {
        String body = """
                {
                    "email":"testregister@gmail.com",
                    "password":"testregisterpassword"
                }
                """;
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    public void loginUser_WrongCredentials() throws Exception{
        String body = """
                {
                    "email":"testregister@gmail.com",
                    "password":"testregisterpassword2"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"Invalid credentials\"}"));
    }

    @Test
    public void loginUser_UserDoesntExist() throws Exception{

        String body = """
                {
                    "email":"testregister2@gmail.com",
                    "password":"testregisterpassword"
                }
                """;
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
