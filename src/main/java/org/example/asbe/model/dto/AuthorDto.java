package org.example.asbe.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link org.example.asbe.model.entity.Author}
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto implements Serializable {
    Long id;
    @NotNull
    @Size(max = 100)
    String name;
    String biography;
    Instant createdAt;
}