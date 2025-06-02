package org.example.asbe.repository;

import org.example.asbe.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByNameContainingIgnoreCaseOrderByIdAsc(String name);

}