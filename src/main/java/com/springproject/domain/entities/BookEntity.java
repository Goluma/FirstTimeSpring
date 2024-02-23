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
@Table(name = "books")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookEntity {

    @Id
    private String isbn;

    private String title;

    @ManyToOne/*(cascade = CascadeType.ALL)*/
    @JoinColumn(name = "author_id")
    private AuthorEntity authorEntity;
}
