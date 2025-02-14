package org.example.asbe.controller;

import jakarta.validation.Valid;
import org.example.asbe.model.entity.Book;
import org.example.asbe.service.impl.BookServiceImpl;
import org.example.asbe.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookServiceImpl service;

    @PostMapping("/addBook")
    public ResponseEntity<?> addNewUser(@RequestBody @Valid Book book, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMessages = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (oldValue, newValue) -> newValue));

            return ResponseUtil.response(HttpStatus.BAD_REQUEST, null, errorMessages, null);
        }
        return ResponseUtil.response(HttpStatus.OK, service.addBook(book), null, null);
    }
}
