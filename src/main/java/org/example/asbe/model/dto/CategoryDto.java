package org.example.asbe.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.asbe.model.entity.Category}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    Long id;
    @NotNull
    @Size(max = 50)
    String name;
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}