package org.example.asbe.controller;

import jakarta.validation.Valid;
import org.example.asbe.model.entity.Author;
import org.example.asbe.model.entity.Category;
import org.example.asbe.service.impl.AuthorServiceImpl;
import org.example.asbe.service.impl.CategoryServiceImpl;
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
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl service;
    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @GetMapping("/list-category")
    public ResponseEntity<?> listCategory(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseUtil.success(service.listCategory(page, size), "List category successfully!");
    }

    @PostMapping("/add-category")
    public ResponseEntity<?> addNewCategory(@RequestBody @Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMessages = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (oldValue, newValue) -> newValue));

            return ResponseUtil.response(HttpStatus.BAD_REQUEST, null, errorMessages, null);
        }
        return ResponseUtil.response(HttpStatus.OK, service.addCategory(category), null, null);
    }

    @PutMapping("/update-category/{id}")
    public ResponseEntity<?> updateCategory(@RequestBody Category category, @PathVariable Integer id) {
        return ResponseUtil.success(categoryServiceImpl.updateCategory(category, id), "Update category successfully!");
    }

    @DeleteMapping("/delete-category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        return ResponseUtil.success(categoryServiceImpl.deleteCategory(id), "Delete successfully!");
    }
}
