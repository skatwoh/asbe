package org.example.asbe.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    Integer id;
    @NotNull
    @Size(max = 255)
    String title;
    @Size(max = 13)
    String isbn;
    LocalDate publicationDate;
    String description;
    @NotNull
    BigDecimal price;
    BigDecimal discountPercent;
    @NotNull
    Integer stockQuantity;
    Integer pageCount;
    @Size(max = 50)
    String language;
    Boolean isRare;
    @Size(max = 50)
    String bookCondition;
    Instant createdAt;
    Instant updatedAt;
    public Set<AuthorDto> authors;
    public Set<CategoryDto> categories;
}
