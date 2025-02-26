package org.example.asbe.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link org.example.asbe.model.entity.Category}
 */
@Value
public class CategoryDto implements Serializable {
    Integer id;
    @NotNull
    @Size(max = 50)
    String name;
    String description;
    Instant createdAt;
}