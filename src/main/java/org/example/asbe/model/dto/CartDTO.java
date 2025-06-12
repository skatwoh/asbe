package org.example.asbe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    Integer id;

    Integer quantity;

    Instant createdAt;

    Instant updatedAt;

    public Set<UserDTO> user;

    public Set<BookDTO> book;
}
