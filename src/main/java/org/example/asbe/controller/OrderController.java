package org.example.asbe.controller;

import org.example.asbe.model.dto.OrderDTO;
import org.example.asbe.model.entity.Order;
import org.example.asbe.service.impl.OrderServiceImpl;
import org.example.asbe.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody OrderDTO order) {
        try {
            String message = orderServiceImpl.createOrderFromCart(order);
            return ResponseUtil.response(HttpStatus.OK, message, null, null);
        } catch (Exception e) {
            return ResponseUtil.response(HttpStatus.BAD_REQUEST, null, Map.of("error", "Invalid input"), null);
        }
    }
}
