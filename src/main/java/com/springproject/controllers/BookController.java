package com.springproject.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springproject.domain.dto.BookDto;
import com.springproject.domain.entities.BookEntity;
import com.springproject.mappers.Mapper;
import com.springproject.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookController {

    private BookService bookService;

    private Mapper<BookEntity, BookDto> bookMapper;

    public BookController(BookService bookService, Mapper<BookEntity, BookDto> bookMapper){
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto){

        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        boolean bookExists = bookService.isExist(isbn);
        BookEntity savedBookEntity = bookService.createUpdateBook(isbn, bookEntity);
        BookDto saveUpdateBook = bookMapper.mapTo(savedBookEntity);
        if (bookExists){
            return new ResponseEntity<>(saveUpdateBook,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(saveUpdateBook,
                    HttpStatus.CREATED);
        }
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto){

        if(!bookService.isExist(isbn)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        BookEntity updatedBook = bookService.partialUpdate(isbn, bookEntity);
        return  new ResponseEntity<>(
                bookMapper.mapTo(updatedBook),
                HttpStatus.OK);
    }

    @GetMapping(path = "/books")
    public Page<BookDto> listBooks(Pageable pageable){
        Page<BookEntity> bookEntities = bookService.findAll(pageable);
        return bookEntities.map(bookMapper::mapTo);
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn){
        Optional<BookEntity> book = bookService.findOne(isbn);
        return book.map(findBook -> {
            BookDto bookDto = bookMapper.mapTo(findBook);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable("isbn") String isbn){
        bookService.delete(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
