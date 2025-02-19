package org.example.asbe.service;

import org.example.asbe.model.dto.BookDTO;
import org.example.asbe.model.entity.Book;
import org.example.asbe.util.dto.PagedResponse;

public interface BookService {

    PagedResponse<BookDTO> listBook(int page, int size);

    String addBook(Book book);

    Book updateBook(Book book, Integer id);

    String deleteBook(Integer id);

}
