package org.example.asbe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.asbe.model.entity.Orderitem;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    Integer id;

    Integer quantity;

    BigDecimal totalAmount;

    String shippingAddress;

    String shippingPhone;

    String shippingName;

    String orderStatus;

    String paymentMethod;

    String paymentStatus;

    Instant createdAt;

    Instant updatedAt;

    Integer userId;

    Set<OrderItemDTO> orderItems;

}
