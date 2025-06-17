package org.example.asbe.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.example.asbe.model.dto.AuthorDto;
import org.example.asbe.model.dto.OrderDTO;
import org.example.asbe.model.dto.OrderItemDTO;
import org.example.asbe.model.dto.UserDTO;
import org.example.asbe.model.entity.*;
import org.example.asbe.repository.*;
import org.example.asbe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private BookRepository bookRepository;

    @Override
    @Transactional
    public String createOrderFromCart(OrderDTO orderInput) {
        try {
            Order order = new Order();
            if (orderInput.getUserId() != null) {
                Userinfo user = userInfoRepository.findById(orderInput.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + orderInput.getUserId()));
                order.setUser(user);
            }
            order.setTotalAmount(orderInput.getTotalAmount());
            order.setShippingAddress(orderInput.getShippingAddress());
            order.setShippingPhone(orderInput.getShippingPhone());
            order.setShippingName(orderInput.getShippingName());
            order.setOrderStatus(orderInput.getOrderStatus() != null ? orderInput.getOrderStatus() : "pending");
            order.setPaymentMethod(orderInput.getPaymentMethod());
            order.setPaymentStatus(orderInput.getPaymentStatus() != null ? orderInput.getPaymentStatus() : "unpaid");
            order.setCreatedAt(Instant.now());
            order.setUpdatedAt(Instant.now());
            Order savedOrder = orderRepository.save(order);
            if (orderInput.getOrderItems() != null && !orderInput.getOrderItems().isEmpty()) {
                Set<Orderitem> orderItems = new LinkedHashSet<>();

                for (OrderItemDTO orderItemDTO : orderInput.getOrderItems()) {

                    Book book = bookRepository.findById(orderItemDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Book not found with id: " + orderItemDTO.getId()));

                    Orderitem orderItem = new Orderitem();
                    OrderitemId orderItemId = new OrderitemId();
                    orderItemId.setOrderId(savedOrder.getId());
                    orderItemId.setBookId(orderItemDTO.getId());
                    orderItem.setId(orderItemId);
                    orderItem.setOrder(savedOrder);
                    orderItem.setBook(book);
                    orderItem.setQuantity(orderItemDTO.getQuantity());
                    orderItem.setPrice(orderItemDTO.getPrice());
                    orderItems.add(orderItem);
                }
                orderItemRepository.saveAll(orderItems);
            }

            return "Thành công";

        } catch (Exception e) {
            throw new RuntimeException("Error saving order: " + e.getMessage(), e);
        }
    }
}
