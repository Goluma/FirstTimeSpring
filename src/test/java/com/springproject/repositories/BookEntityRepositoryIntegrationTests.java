package com.springproject.repositories;

import com.springproject.TestDataUtil;
import com.springproject.domain.entities.AuthorEntity;
import com.springproject.domain.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationTests {


    private BookRepository underTest;

    @Autowired
    public BookEntityRepositoryIntegrationTests(BookRepository underTest){
        this.underTest = underTest;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled(){

        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        BookEntity bookEntity = TestDataUtil.createTestBookA(authorEntity);
        underTest.save(bookEntity);
        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatMultiplyBooksCanBeCreatedAndRecalled(){

        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorB();
        AuthorEntity authorEntityC = TestDataUtil.createTestAuthorC();


        BookEntity bookEntityA = TestDataUtil.createTestBookA(authorEntityA);
        underTest.save(bookEntityA);
        BookEntity bookEntityB = TestDataUtil.createTestBookB(authorEntityB);
        underTest.save(bookEntityB);
        BookEntity bookEntityC = TestDataUtil.createTestBookC(authorEntityC);
        underTest.save(bookEntityC);

        Iterable<BookEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(bookEntityA, bookEntityB, bookEntityC);
    }

    @Test
    public void testThatBooksCanBeUpdated(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();

        BookEntity bookEntity = TestDataUtil.createTestBookA(authorEntityA);
        underTest.save(bookEntity);
        bookEntity.setTitle("UPDATED");

        underTest.save(bookEntity);

        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatBookCanBeDeleted(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();

        BookEntity bookEntity = TestDataUtil.createTestBookA(authorEntityA);
        underTest.save(bookEntity);

        underTest.deleteById(bookEntity.getIsbn());

        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result).isEmpty();
    }
}
