package org.example.asbe.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.example.asbe.model.dto.*;
import org.example.asbe.model.entity.*;
import org.example.asbe.repository.*;
import org.example.asbe.service.OrderService;
import org.example.asbe.util.CustomException;
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
            log.info("===> Bắt đầu tạo Order từ DTO");

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
            log.info("===> Đã lưu Order ID = {}", savedOrder.getId());

            if (orderInput.getOrderItems() != null && !orderInput.getOrderItems().isEmpty()) {
                Set<Orderitem> orderItems = new LinkedHashSet<>();

                for (OrderItemDTO itemDTO : orderInput.getOrderItems()) {
                    Book book = bookRepository.findById(itemDTO.getBookId())
                            .orElseThrow(() -> new RuntimeException("Book not found with id: " + itemDTO.getBookId()));

                    OrderitemId id = new OrderitemId();
                    id.setOrderId(savedOrder.getId());
                    id.setBookId(itemDTO.getBookId());

                    Orderitem item = new Orderitem();
                    item.setId(id);
                    item.setOrder(savedOrder);
                    item.setBook(book);
                    item.setQuantity(itemDTO.getQuantity());
                    item.setPrice(itemDTO.getPrice());
                    orderItems.add(item);
                    Cart cart = cartRepository.findAllCartWithUserAndBook(orderInput.getUserId(),id.getBookId())
                            .orElseThrow(() -> new RuntimeException("Cart not found"));
                    cart.setStatus(true);
                    book.setStockQuantity(book.getStockQuantity() - itemDTO.getQuantity());
                    bookRepository.save(book);
                    cartRepository.save(cart);
                }

                orderItemRepository.saveAll(orderItems);
                log.info("===> Đã lưu {} OrderItems", orderItems.size());
            }

            return savedOrder.getId().toString();

        } catch (Exception e) {
            log.error("Lỗi khi tạo đơn hàng: {}", e.getMessage(), e);
            throw new CustomException("Lỗi khi tạo đơn hàng: " + e.getMessage());
        }
    }

}
