package com.springproject.services.impl;

import com.springproject.domain.entities.AuthorEntity;
import com.springproject.repositories.AuthorRepository;
import com.springproject.services.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        return authorRepository.save(authorEntity);
    }

    @Override
    public List<AuthorEntity> findAll() {
        return StreamSupport.stream(authorRepository
                        .findAll()
                        .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AuthorEntity> findAll(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    @Override
    public Optional<AuthorEntity> findOne(Long id) {
        return authorRepository.findById(id);
    }


    @Override
    public boolean isExist(Long id) {
        return authorRepository.existsById(id);
    }

    @Override
    public AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity) {
        authorEntity.setId(id);

        return authorRepository.findById(id).map(existingAuthor ->{
            Optional.ofNullable(authorEntity.getAge()).ifPresent(existingAuthor::setAge);
            Optional.ofNullable(authorEntity.getName()).ifPresent(existingAuthor::setName);
            return authorRepository.save(existingAuthor);
        }).orElseThrow(() -> new RuntimeException("Author doesn't exist"));
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
