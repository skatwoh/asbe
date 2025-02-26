package org.example.asbe.service;

import org.example.asbe.model.dto.CategoryDto;
import org.example.asbe.model.entity.Category;
import org.example.asbe.util.dto.PagedResponse;

public interface CategoryService {
    PagedResponse<CategoryDto> listCategory(int page, int size);

    String addCategory(Category category);

    Category updateCategory(Category category, Integer id);

    String deleteCategory(Integer id);
}
