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
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookServiceImpl service;
    @Autowired
    private BookServiceImpl bookServiceImpl;

    @GetMapping("/list-book")
    public ResponseEntity<?> listBook(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseUtil.success(service.listBook(page, size), "List user successfully!");
    }

    @PostMapping("/add-book")
    public ResponseEntity<?> addNewBook(@RequestBody @Valid Book book, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMessages = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (oldValue, newValue) -> newValue));

            return ResponseUtil.response(HttpStatus.BAD_REQUEST, null, errorMessages, null);
        }
        return ResponseUtil.response(HttpStatus.OK, service.addBook(book), null, null);
    }

    @PutMapping("/update-book/{id}")
    public ResponseEntity<?> updateBook(@RequestBody Book book, @PathVariable Integer id) {
        return ResponseUtil.success(bookServiceImpl.updateBook(book, id), "Update book successfully!");
    }

    @DeleteMapping("/delete-book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer id) {
        return ResponseUtil.success(bookServiceImpl.deleteBook(id), "Delete successfully!");
    }
}
