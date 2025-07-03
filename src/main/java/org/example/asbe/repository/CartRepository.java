package org.example.asbe.repository;

import org.example.asbe.model.dto.CartDTO;
import org.example.asbe.model.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserIdAndBookId(Integer userId, Integer bookId);

    Optional<Cart> findByUserIdAndBookIdAndStatus(Integer userId, Integer bookId, Boolean status);

    List<Cart> findByUserIdAndStatus(Integer userId, Boolean status);

    @Query(value = """
            SELECT c.cart_id AS cart_id,
                   c.quantity AS quantity,
                   u.user_id AS user_id,
                   u.username AS user_name,
                   b.book_id AS book_id,
                   b.title AS book_title,
                   c.created_at AS created_at,
                   c.updated_at AS updated_at,
                   c.status AS status
            FROM cart c
            JOIN userinfo u ON c.user_id = u.user_id
            JOIN books b ON c.book_id = b.book_id
            WHERE c.status = false AND c.user_id = :userId AND c.book_id = :bookId
            """, nativeQuery = true)
    Optional<Cart> findAllCartWithUserAndBook(Integer userId, Integer bookId);

}

