package org.example.asbe.service;

import org.example.asbe.model.dto.OrderDTO;

public interface OrderService {
    String createOrderFromCart(OrderDTO order);
}
