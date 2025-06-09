package org.example.asbe.repository;

import org.example.asbe.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findBookByTitle(String title);

    @Query(value = "SELECT b.*\n" +
            "FROM books b\n" +
            "         JOIN bookcategories bc ON b.book_id = bc.book_id\n" +
            "         JOIN categories c ON bc.category_id = c.category_id\n" +
            "WHERE LOWER(c.name) LIKE LOWER(:category)", nativeQuery = true)
    Page<Book> findBooksByCategoryNameContaining(@Param("category") String category, Pageable pageable);


}
