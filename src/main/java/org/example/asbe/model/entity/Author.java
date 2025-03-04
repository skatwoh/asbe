package org.example.asbe.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('authors_author_id_seq')")
    @Column(name = "author_id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull(message = "Name is required")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "biography", length = Integer.MAX_VALUE)
    private String biography;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

}