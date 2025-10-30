package com.example.steplang.controllers;

import com.example.steplang.BaseTest;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;
    private static String jwtToken;

    @Test
    @Order(1)
    void registerAndLoginUser() throws Exception {
        //Register 1st User
        String registerJson = """
                {
                    "username":"John",
                    "email":"John@gmail.com",
                    "password":"JohnPassword"
                }
                """;
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk());

        //Register 2nd User
        String registerJson2 = """
                {
                    "username":"Anna",
                    "email":"Anna@gmail.com",
                    "password":"AnnaPassword"
                }
                """;
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson2))
                .andExpect(status().isOk());

        //Login 1st User
        String loginJson = """
                {
                    "email":"John@gmail.com",
                    "password":"JohnPassword"
                }
                """;
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        jwtToken = JsonPath.parse(responseBody).read("$.accessToken").toString();
    }

    @Test
    public void GetMeInformationTest() throws Exception {
        System.out.println(jwtToken);
        mockMvc.perform(get("/api/user/me")
                        .header("Authorization","Bearer "+ jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"username\":\"John\",\"email\":\"John@gmail.com\",\"level\":1,\"exp\":0}"));
    }
    @Test
    public void GetUserProfileInformationTest() throws Exception {
        System.out.println(jwtToken);
        mockMvc.perform(get("/api/user/2")
                        .header("Authorization","Bearer "+ jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":2,\"username\":\"Anna\",\"level\":1,\"exp\":0}"));
    }
}
