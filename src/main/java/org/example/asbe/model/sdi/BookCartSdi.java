package org.example.asbe.model.sdi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookCartSdi {
    Integer userId;

    Integer bookId;

    Integer quantity;

}
