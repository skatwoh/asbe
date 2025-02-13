package org.example.asbe.controller;

import jakarta.validation.Valid;
import org.example.asbe.entity.Book;
import org.example.asbe.service.BookService;
import org.example.asbe.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService service;

    @PostMapping("/addBook")
    public ResponseEntity<?> addNewUser(@RequestBody @Valid Book book) {
        return ResponseUtil.response(HttpStatus.OK, service.addBook(book), null, null);
    }
}
