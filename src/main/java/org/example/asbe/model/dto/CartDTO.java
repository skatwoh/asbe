package org.example.asbe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.asbe.model.entity.Book;
import org.example.asbe.model.entity.Userinfo;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    Integer id;

    Integer quantity;

    Instant createdAt;

    Instant updatedAt;

    Integer userId;

    Integer bookId;

    String username;

    String bookName;

    BookDTO book;
    
    UserDTO user;
}
