package org.example.asbe.repository;

import org.example.asbe.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserIdAndBookId(Integer userId, Integer bookId);

    List<Cart> findByUserIdAndStatus(Integer userId, Boolean status);
}

