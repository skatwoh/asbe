package org.example.asbe.controller;

import org.example.asbe.model.sdi.BookCartSdi;
import org.example.asbe.service.impl.CartServiceImpl;
import org.example.asbe.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartServiceImpl cartServiceImpl;

    @GetMapping("/list-cart")
    public ResponseEntity<?> listCart(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseUtil.success(cartServiceImpl.listCart(page, size), "List cart successfully!");
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addBookToCart(@RequestBody BookCartSdi bookCartSdi) {
        try {
            String message = cartServiceImpl.addBookToCart(bookCartSdi);
            return ResponseUtil.response(HttpStatus.OK, message, null, null);
        } catch (Exception e) {
            return ResponseUtil.response(HttpStatus.BAD_REQUEST, null, Map.of("error", "Invalid input"), null);
        }
    }

    @DeleteMapping("/delete-book-from-cart/{id}")
    public ResponseEntity<?> deleteBookFromCart(@PathVariable Long id) {
        return ResponseUtil.success(cartServiceImpl.deleteBookFromCart(id), "Delete successfully!");
    }
}
