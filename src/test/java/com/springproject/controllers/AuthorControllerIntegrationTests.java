package com.springproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.TestDataUtil;
import com.springproject.domain.dto.AuthorDto;
import com.springproject.domain.entities.AuthorEntity;
import com.springproject.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTests {

    private AuthorService authorService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, AuthorService authorService){
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.authorService = authorService;
    }

    @Test
    public void testThatCreateAuthorReturnsHttp201Created() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        authorEntity.setId(null);
        String authorJson = objectMapper.writeValueAsString(authorEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        authorEntity.setId(null);
        String authorJson = objectMapper.writeValueAsString(authorEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Kashitsyn Evgeny")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(19)
        );
    }
    @Test
    public void testThatListAuthorsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
        authorService.save(TestDataUtil.createTestAuthorA());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Kashitsyn Evgeny")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(19)
        );
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        authorService.save(TestDataUtil.createTestAuthorA());
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus404WhenAuthorNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + 10)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAuthorReturnsAuthor() throws Exception {
        authorService.save(TestDataUtil.createTestAuthorA());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Kashitsyn Evgeny")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(19)
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {

        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(authorEntity);

        ObjectMapper objectMapper1 = new ObjectMapper();
        String json = objectMapper1.writeValueAsString(TestDataUtil.createTestAuthorDtoA());

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus404WhenAuthorNotExists() throws Exception {
        ObjectMapper objectMapper1 = new ObjectMapper();
        String json = objectMapper1.writeValueAsString(authorService.save(TestDataUtil.createTestAuthorA()));

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(authorEntity);

        ObjectMapper objectMapper1 = new ObjectMapper();
        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        String json = objectMapper1.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(savedAuthor.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(savedAuthor.getAge())
        );
    }

    @Test
    public void testThatPartialUpdateReturnsHttp200() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(authorEntity);

        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        authorDto.setName("UPDATED");
        ObjectMapper objectMapper1 = new ObjectMapper();
        String json = objectMapper1.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateReturnsHttp404WhenAuthorNotExists() throws Exception {

        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        authorDto.setName("UPDATED");
        ObjectMapper objectMapper1 = new ObjectMapper();
        String json = objectMapper1.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + 20)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateAuthorReturnsAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(authorEntity);

        ObjectMapper objectMapper1 = new ObjectMapper();
        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        authorDto.setName("UPDATED");
        String json = objectMapper1.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDto.getAge())
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHttp204ForExistingAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHttp204ForNoExistingAuthor() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" + 1000)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
