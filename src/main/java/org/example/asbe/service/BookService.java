package org.example.asbe.service;

import org.example.asbe.model.dto.BookDTO;
import org.example.asbe.model.entity.Book;
import org.example.asbe.util.dto.PagedResponse;

import java.util.Set;

public interface BookService {

    PagedResponse<BookDTO> listBook(int page, int size);

    String addBook(BookDTO book, Set<Long> authorIds, Set<Long> categoryIds);

    Book updateBook(Book book, Integer id);

    String deleteBook(Integer id);

}
