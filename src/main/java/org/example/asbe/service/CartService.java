package org.example.asbe.service;

import org.example.asbe.model.dto.AuthorDto;
import org.example.asbe.model.dto.CartDTO;
import org.example.asbe.model.sdi.BookCartSdi;
import org.example.asbe.util.dto.PagedResponse;

public interface CartService {
//    String addBookToCart(List<BookCartSdi> listBookCart);

    String addBookToCart(BookCartSdi bookCartSdi);

    PagedResponse<CartDTO> listCart(int page, int size);

    String deleteBookFromCart(Long id);
}
