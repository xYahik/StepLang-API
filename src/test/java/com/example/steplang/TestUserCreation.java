package com.example.steplang;

import com.jayway.jsonpath.JsonPath;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUserCreation {
    @Getter
    private static String accessToken;
    private static boolean initialized = false;

    public static synchronized void init(MockMvc mockMvc) throws Exception{
        if(initialized) return;

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
                        .content(registerJson));
                //.andExpect(status().isOk());

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
        accessToken = JsonPath.parse(responseBody).read("$.accessToken").toString();
        initialized = true;
    }

    public static void clearToken() {
        initialized = false;
        accessToken = null;
    }
}
