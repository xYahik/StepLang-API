package com.example.steplang.controllers;

import com.example.steplang.TestUserCreation;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest extends BaseControllerTest {

    @Test
    public void GetMeInformationTest() throws Exception {
        mockMvc.perform(get("/api/user/me")
                .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"username\":\"John\",\"email\":\"John@gmail.com\",\"level\":1,\"exp\":0}"));
    }
    @Test
    public void GetUserProfileInformationTest() throws Exception {
        mockMvc.perform(get("/api/user/1")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"username\":\"John\",\"level\":1,\"exp\":0}"));
    }
}