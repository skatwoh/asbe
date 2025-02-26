package org.example.asbe.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link org.example.asbe.model.entity.Author}
 */
@Value
public class AuthorDto implements Serializable {
    Integer id;
    @NotNull
    @Size(max = 100)
    String name;
    String biography;
    Instant createdAt;
}