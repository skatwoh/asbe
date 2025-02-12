package org.example.asbe.service;

import jakarta.validation.constraints.NotNull;
import org.example.asbe.entity.Book;
import org.example.asbe.repository.BookRepository;
import org.example.asbe.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private BookRepository repository;

    @Autowired
    private MessageUtil messageUtil;

    public String addBook(Book book) {
        repository.save(book);
        return messageUtil.getMessage("success");
    }
}
