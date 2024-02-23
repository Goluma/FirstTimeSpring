package com.springproject;

import com.springproject.domain.dto.AuthorDto;
import com.springproject.domain.dto.BookDto;
import com.springproject.domain.entities.AuthorEntity;
import com.springproject.domain.entities.BookEntity;

public final class TestDataUtil {

    private TestDataUtil(){}

    public static AuthorEntity createTestAuthorA() {
        return AuthorEntity.builder()
                .id(1L)
                .name("Kashitsyn Evgeny")
                .age(19)
                .build();
    }

    public static AuthorDto createTestAuthorDtoA() {
        return AuthorDto.builder()
                .id(1L)
                .name("Kashitsyn Evgeny")
                .age(19)
                .build();
    }

    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder()
                .id(2L)
                .name("J. R. R. Tolkien")
                .age(100)
                .build();
    }

    public static AuthorEntity createTestAuthorC() {
        return AuthorEntity.builder()
                .id(3L)
                .name("Lev Tolstoy")
                .age(150)
                .build();
    }


    public static BookEntity createTestBookA(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("9-000-567-12")
                .title("The man who changed everything")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookEntity createTestBookB(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("9-090-333-00")
                .title("The Lord of the Rings")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookDto createTestBookDtoB(final AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("9-090-333-00")
                .title("The Lord of the Rings")
                .authorEntity(authorDto)
                .build();
    }

    public static BookEntity createTestBookC(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("9-111-234-90")
                .title("War and Peace")
                .authorEntity(authorEntity)
                .build();
    }
}
