package org.example.asbe.service;

import jakarta.validation.constraints.NotNull;
import org.example.asbe.entity.Book;
import org.example.asbe.repository.BookRepository;
import org.example.asbe.util.CustomException;
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
        if(repository.findBookByTitle(book.getTitle()).isPresent()) {
//            return messageUtil.getMessage("error.book.exists", book.getTitle());
            throw new CustomException(messageUtil.getMessage("error.book.exists", book.getTitle()));
        }
        repository.save(book);
        return messageUtil.getMessage("success");
    }
}
