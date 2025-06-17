package org.example.asbe.repository;

import org.example.asbe.model.entity.Orderitem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<Orderitem, Long> {
}
