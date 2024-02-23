package com.springproject.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "authors")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorEntity {

    @Id
    @SequenceGenerator(name = "hibernate_seq", sequenceName = "hibernate_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_seq")
    private Long id;

    private String name;

    private Integer age;

}


