package com.example.steplang.controllers;

import com.example.steplang.TestUserCreation;
import com.example.steplang.errors.LanguageError;
import com.example.steplang.repositories.language.LanguageRepository;
import com.example.steplang.repositories.language.WordCategoryRepository;
import com.example.steplang.repositories.language.WordRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LanguageControllerTest extends BaseControllerTest {
    @Autowired
    private WordRepository wordRepo;
    @Autowired
    private LanguageRepository languageRepo;
    @Autowired
    private WordCategoryRepository wordCategoryRepo;

    @Test
    public void addNewLanguage() throws Exception{
        String body = """
                {
                    "name":"Czech"
                }
                """;

        //Add new language to database
        mockMvc.perform(post("/api/language/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());


        //Add same language to database
        mockMvc.perform(post("/api/language/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(LanguageError.ALREADY_EXISTS.toString()));

        //Try add language with wrong body
        mockMvc.perform(post("/api/language/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name2\": \"Czech\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(LanguageError.MISSING_KEY.toString()));
    }

    @Test
    public void patchLanguage() throws Exception{
        String body = """
                {
                    "name":"Czech"
                }
                """;

        //Add new language to database
        MvcResult result = mockMvc.perform(post("/api/language/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        Long createdLangaugeID = JsonPath
                .parse(result.getResponse().getContentAsString())
                .read("$.id", Long.class);

        //update language information
        mockMvc.perform(patch("/api/language/"+createdLangaugeID)
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"English\"}"))
                .andExpect(status().isOk());

        //update language information with wrong key
        mockMvc.perform(patch("/api/language/"+createdLangaugeID)
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name2\": \"Polish\"}"))
                .andExpect(status().isOk());

        //check if language name changed
        mockMvc.perform(get("/api/language/"+createdLangaugeID)
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("English"));
    }

    @Test
    public void getAllLanguageWords() throws Exception {
        //Add new language to database
        MvcResult result = mockMvc.perform(post("/api/language/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Czech\"}"))
                .andExpect(status().isOk())
                .andReturn();

        Long createdLanguageID = JsonPath
                .parse(result.getResponse().getContentAsString())
                .read("$.id", Long.class);

        result = mockMvc.perform(post("/api/language/"+createdLanguageID+"/word/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\": \"Ahoj\", \"translation\":\"Hello\"}"))
                .andExpect(status().isOk())
                .andReturn();

        result = mockMvc.perform(post("/api/language/"+createdLanguageID+"/word/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\": \"Ahoj2\", \"translation\":\"Hello\"}"))
                .andExpect(status().isOk())
                .andReturn();


        //get all words
        mockMvc.perform(get("/api/language/"+createdLanguageID+"/words")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    public void addWords() throws Exception{

        MvcResult result = mockMvc.perform(post("/api/language/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Czech\"}"))
                .andExpect(status().isOk())
                .andReturn();

        Long createdLanguageID = JsonPath
                .parse(result.getResponse().getContentAsString())
                .read("$.id", Long.class);


        result = mockMvc.perform(post("/api/language/"+createdLanguageID+"/word/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\": \"Ahoj\", \"translation\":\"Hello\"}"))
                .andExpect(status().isOk())
                .andReturn();

        Long createdWordID = JsonPath
                .parse(result.getResponse().getContentAsString())
                .read("$.wordId", Long.class);

        result = mockMvc.perform(post("/api/language/"+createdLanguageID+"/word/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\": \"Ahoj2\", \"translation\":\"Hello\"}"))
                .andExpect(status().isOk())
                .andReturn();

        Long createdWordID2 = JsonPath
                .parse(result.getResponse().getContentAsString())
                .read("$.wordId", Long.class);

        mockMvc.perform(get("/api/language/"+createdLanguageID+"/"+createdWordID)
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(get("/api/language/"+createdLanguageID+"/"+createdWordID2)
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));

        mockMvc.perform(get("/api/language/"+createdLanguageID+"/"+createdWordID2)
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wordId").value(2));

    }

    @Test
    public void PatchWord() throws Exception{
        MvcResult result = mockMvc.perform(post("/api/language/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Czech\"}"))
                .andExpect(status().isOk())
                .andReturn();

        Long createdLanguageID = JsonPath
                .parse(result.getResponse().getContentAsString())
                .read("$.id", Long.class);


        result = mockMvc.perform(post("/api/language/"+createdLanguageID+"/word/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\": \"Ahoj\", \"translation\":\"Hello\"}"))
                .andExpect(status().isOk())
                .andReturn();

        Long createdWordID = JsonPath
                .parse(result.getResponse().getContentAsString())
                .read("$.wordId", Long.class);

        //update language information
        mockMvc.perform(patch("/api/language/"+createdLanguageID+"/"+createdWordID)
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\": \"English\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/language/"+createdLanguageID+"/"+createdWordID)
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word").value("English"));
    }


    @Test
    public void addCategory() throws Exception{
        MvcResult result = mockMvc.perform(post("/api/language/category/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"House\"}"))
                .andExpect(status().isOk())
                .andReturn();


        mockMvc.perform(get("/api/language/category/1")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("House"));

    }
    @Test
    public void patchCategory() throws Exception{
        MvcResult result = mockMvc.perform(post("/api/language/category/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"House\"}"))
                .andExpect(status().isOk())
                .andReturn();


        mockMvc.perform(patch("/api/language/category/1")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Food\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/language/category/1")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Food"));

    }

    @Test
    public void deleteLanguages() throws Exception{
        mockMvc.perform(post("/api/language/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Czech\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/language/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"English\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/language/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Polish\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/language/3")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/language/")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(List.of(2, 3).toString()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/language/3")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(LanguageError.LANGUAGE_ID_NOT_FOUND.toString()));

        mockMvc.perform(get("/api/language/1")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/language/1")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/language/1")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(LanguageError.LANGUAGE_ID_NOT_FOUND.toString()));

    }
    @Test
    public void deleteWords() throws Exception{

        mockMvc.perform(post("/api/language/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Czech\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/language/1/word/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\": \"Ahoj\",\"translation\": \"Hello\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/language/1/word/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\": \"Ahoj2\",\"translation\": \"Hello2\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/language/1/word/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\": \"Ahoj3\",\"translation\": \"Hello3\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/language/1/3")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/language/1/words")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(List.of(2, 3).toString()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/language/1/3")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(LanguageError.LANGUAGE_AND_WORD_ID_NOT_FOUND.toString()));

        mockMvc.perform(get("/api/language/1/1")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/language/1/1")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/language/1/1")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(LanguageError.LANGUAGE_AND_WORD_ID_NOT_FOUND.toString()));

    }
    @Test
    public void deleteCategory() throws Exception{

        mockMvc.perform(post("/api/language/category/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"House\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/language/category/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Food\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/language/category/add")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Restaurant\"}"))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/language/category/3")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/language/category")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(List.of(2, 3).toString()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/language/category/3")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(LanguageError.CATEGORY_ID_NOT_FOUND.toString()));

        mockMvc.perform(get("/api/language/category/1")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/language/category/1")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/language/category/1")
                        .header("Authorization","Bearer "+ TestUserCreation.getAccessToken()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(LanguageError.CATEGORY_ID_NOT_FOUND.toString()));

    }

}
