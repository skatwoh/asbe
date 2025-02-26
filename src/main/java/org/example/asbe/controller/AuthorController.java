package org.example.asbe.controller;

import jakarta.validation.Valid;
import org.example.asbe.model.entity.Author;
import org.example.asbe.service.impl.AuthorServiceImpl;
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
@RequestMapping("/author")
public class AuthorController {
    @Autowired
    private AuthorServiceImpl service;
    @Autowired
    private AuthorServiceImpl authorServiceImpl;

    @GetMapping("/list-author")
    public ResponseEntity<?> listAuthor(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseUtil.success(service.listAuthor(page, size), "List author successfully!");
    }

    @PostMapping("/add-author")
    public ResponseEntity<?> addNewAuthor(@RequestBody @Valid Author author, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMessages = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (oldValue, newValue) -> newValue));

            return ResponseUtil.response(HttpStatus.BAD_REQUEST, null, errorMessages, null);
        }
        return ResponseUtil.response(HttpStatus.OK, service.addAuthor(author), null, null);
    }

    @PutMapping("/update-author/{id}")
    public ResponseEntity<?> updateAuthor(@RequestBody Author author, @PathVariable Integer id) {
        return ResponseUtil.success(authorServiceImpl.updateAuthor(author, id), "Update author successfully!");
    }

    @DeleteMapping("/delete-author/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Integer id) {
        return ResponseUtil.success(authorServiceImpl.deleteAuthor(id), "Delete successfully!");
    }
}
