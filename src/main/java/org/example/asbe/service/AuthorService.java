package org.example.asbe.service;

import org.example.asbe.model.dto.AuthorDto;
import org.example.asbe.model.entity.Author;
import org.example.asbe.util.dto.PagedResponse;

public interface AuthorService {
    PagedResponse<AuthorDto> listAuthor(int page, int size);

    String addAuthor(Author author);

    Author updateAuthor(Author author, Integer id);

    String deleteAuthor(Integer id);
}
