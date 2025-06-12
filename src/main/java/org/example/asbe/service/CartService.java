package org.example.asbe.service;

import org.example.asbe.model.sdi.BookCartSdi;

import java.util.List;

public interface CartService {
    String addBookToCart(List<BookCartSdi> listBookCart);
}
