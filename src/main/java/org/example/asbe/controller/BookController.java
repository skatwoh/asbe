package org.example.asbe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.example.asbe.model.dto.BookDTO;
import org.example.asbe.model.entity.Book;
import org.example.asbe.service.BookService;
import org.example.asbe.service.impl.AuthorServiceImpl;
import org.example.asbe.service.impl.BookServiceImpl;
import org.example.asbe.util.ResponseUtil;
import org.example.asbe.util.dto.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService service;
    @Autowired
    private BookServiceImpl bookServiceImpl;
    @Autowired
    private AuthorServiceImpl authorServiceImpl;

//    @GetMapping("/list-book")
//    public ResponseEntity<?> listBook(
//            @RequestParam(value = "page", defaultValue = "1") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size,
//            @RequestParam(value = "category", defaultValue = "") String category)
//    {
//        return ResponseUtil.success(service.listBook(page, size, category), "List books successfully!");
//    }

    @GetMapping("/list-book")
    public ResponseEntity<?> listBook(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "category",required = false) List<String> category
    ) {
        return ResponseUtil.success(bookServiceImpl.listBook(page, size,category));
    }

    @PostMapping("/add-book")
    public ResponseEntity<?> addNewBook(@RequestBody @Valid BookDTO book, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMessages = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (oldValue, newValue) -> newValue));

            return ResponseUtil.response(HttpStatus.BAD_REQUEST, null, errorMessages, null);
        }
        return ResponseUtil.response(HttpStatus.OK, service.addBook(book, book.authors, book.categories), null, null);
    }

    @PutMapping("/update-book/{id}")
    public ResponseEntity<?> updateBook(@RequestBody Book book, @PathVariable Integer id) {
        return ResponseUtil.success(bookServiceImpl.updateBook(book, id), "Update book successfully!");
    }

    @DeleteMapping("/delete-book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer id) {
        return ResponseUtil.success(bookServiceImpl.deleteBook(id), "Delete successfully!");
    }

    @GetMapping("/get-book/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Integer id) {
        Book book = service.getBook(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
