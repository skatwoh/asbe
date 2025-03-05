package org.example.asbe.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link org.example.asbe.model.entity.Category}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto implements Serializable {
    Long id;
    @NotNull
    @Size(max = 50)
    String name;
    String description;
    Instant createdAt;
}