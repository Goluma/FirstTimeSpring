package com.springproject.repositories;

import com.springproject.domain.entities.AuthorEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<AuthorEntity, Long>,
        PagingAndSortingRepository<AuthorEntity, Long> {

//    Iterable<AuthorEntity> ageLessThan(int age);
//
//    @Query("Select a from Author a where a.age > ?1")
//    Iterable<AuthorEntity> findAuthorsWithAgeGreaterThan(int age);
}
