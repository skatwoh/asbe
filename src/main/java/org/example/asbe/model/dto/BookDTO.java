package org.example.asbe.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.asbe.model.entity.Author;
import org.example.asbe.model.entity.Category;

import java.io.Serializable;
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

    String title;

    String isbn;
    LocalDate publicationDate;
    String description;
    Double price;
    BigDecimal discountPercent;

    Integer stockQuantity;
    Integer quantity;
    Integer pageCount;

    String language;
    Boolean isRare;

    String bookCondition;
    Instant createdAt;
    Instant updatedAt;
    public Set<AuthorDto> authors;
    public Set<CategoryDto> categories;
}
