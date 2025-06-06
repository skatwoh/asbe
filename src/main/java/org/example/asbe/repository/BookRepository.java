package org.example.asbe.repository;

import org.example.asbe.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findBookByTitle(String title);

//    Optional<Book> findBookByAuthor(String author);

}
