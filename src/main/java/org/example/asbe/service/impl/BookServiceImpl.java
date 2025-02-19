package org.example.asbe.service.impl;

import org.example.asbe.model.entity.Book;
import org.example.asbe.repository.BookRepository;
import org.example.asbe.service.BookService;
import org.example.asbe.util.CustomException;
import org.example.asbe.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
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

    public Book updateBook(Book book, Integer id) {
        if(repository.existsById(id)){
            Book existingBook = repository.findById(id).get();
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setGenre(book.getGenre());
            existingBook.setPublished(book.getPublished());
            existingBook.setPublisher(book.getPublisher());
            existingBook.setDescription(book.getDescription());
            existingBook.setImageUrl(book.getImageUrl());
            return repository.save(book);
        }
        return  null;
    }

}
