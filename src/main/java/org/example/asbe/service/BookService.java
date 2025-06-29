package org.example.asbe.service;

import org.example.asbe.model.dto.AuthorDto;
import org.example.asbe.model.dto.BookDTO;
import org.example.asbe.model.dto.CategoryDto;
import org.example.asbe.model.entity.Author;
import org.example.asbe.model.entity.Book;
import org.example.asbe.model.entity.Category;
import org.example.asbe.util.dto.PagedResponse;

import java.util.Set;

public interface BookService {

    PagedResponse<BookDTO> listBook(int page, int size, String category);

    String addBook(BookDTO book, Set<AuthorDto> authors, Set<CategoryDto> categories);

    Book updateBook(Book book, Integer id);

    String deleteBook(Integer id);

    Book getBook(Integer id);

}
