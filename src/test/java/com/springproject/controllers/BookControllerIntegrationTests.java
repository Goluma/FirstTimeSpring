package com.springproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.TestDataUtil;
import com.springproject.domain.dto.BookDto;
import com.springproject.domain.entities.BookEntity;
import com.springproject.services.BookService;
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
public class BookControllerIntegrationTests {

    private BookService bookService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, BookService bookService){
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.bookService = bookService;
    }

    @Test
    public void testThatCreateBookReturnsHttp201Created() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookA(null);

        String bookJson = objectMapper.writeValueAsString(bookEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateBookReturnsCreatedBook() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookA(null);

        String bookJson = objectMapper.writeValueAsString(bookEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookEntity.getTitle())
        );
    }

    @Test
    public void testThatListBooksReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAuthorsReturnsListOfBooks() throws Exception {
        BookEntity bookEntity = bookService.createUpdateBook("111-11-111-11", TestDataUtil.createTestBookA( null));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].isbn").value(bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value(bookEntity.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].authorEntity").isEmpty()
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus200WhenBookExists() throws Exception {
        bookService.createUpdateBook("111-11-111-11", TestDataUtil.createTestBookA(null));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + "111-11-111-11")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus404WhenBookNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + "222-22-222-22")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetBookReturnsBook() throws Exception {
        bookService.createUpdateBook("111-11-111-11", TestDataUtil.createTestBookA(null));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + "111-11-111-11")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value("111-11-111-11")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("The man who changed everything")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.authorEntity").isEmpty()
        );
    }

    @Test
    public void testThatPutBookReturnsHttpStatus200WhenBookExists() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookB(null);
        BookEntity savedBookEntity = bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        BookDto bookDto = TestDataUtil.createTestBookDtoB(null);
        bookDto.setIsbn(savedBookEntity.getIsbn());
        ObjectMapper objectMapper1 = new ObjectMapper();
        String json = objectMapper1.writeValueAsString(bookDto);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPutBookReturnsHttpStatus201WhenBookNotExists() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        ObjectMapper objectMapper1 = new ObjectMapper();
        String json = objectMapper1.writeValueAsString(bookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + "33-333-33")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatPutUpdatedBookReturnsUpdatedBook() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookB(null);
        BookEntity savedBookEntity = bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        BookDto bookDto = TestDataUtil.createTestBookDtoB(null);
        bookDto.setIsbn(savedBookEntity.getIsbn());
        bookDto.setTitle("UPDATED");
        ObjectMapper objectMapper1 = new ObjectMapper();
        String json = objectMapper1.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.authorEntity").isEmpty()
        );
    }

    @Test
    public void testThatPartialUpdateReturnsHttp200() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookB(null);
        BookEntity savedBook = bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        ObjectMapper objectMapper1 = new ObjectMapper();
        BookDto bookDto = TestDataUtil.createTestBookDtoB(null);
        bookDto.setTitle("UPDATED");
        String json = objectMapper1.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateReturnsHttp404WhenBookNotExists() throws Exception {

        ObjectMapper objectMapper1 = new ObjectMapper();
        BookDto bookDto = TestDataUtil.createTestBookDtoB(null);
        bookDto.setTitle("UPDATED");
        String json = objectMapper1.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + 20)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnsBook() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookB(null);
        BookEntity savedBook = bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        ObjectMapper objectMapper1 = new ObjectMapper();
        BookDto bookDto = TestDataUtil.createTestBookDtoB(null);
        bookDto.setTitle("UPDATED");
        String json = objectMapper1.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.authorEntity").value(bookDto.getAuthorEntity())
        );
    }

    @Test
    public void testThatDeleteBookReturnsHttp204ForExistingBook() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        BookEntity savedBook = bookService.createUpdateBook("111", bookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteBookReturnsHttp204ForNoExistingBook() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + 1000)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
