package org.example.asbe.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.asbe.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    boolean existsByName(@Size(max = 100) @NotNull String name);

    boolean existsByBiography(String biography);

    List<Author> findByNameContainingIgnoreCaseOrderById(String biography);
}