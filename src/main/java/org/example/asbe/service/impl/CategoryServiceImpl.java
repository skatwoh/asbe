package org.example.asbe.service.impl;

import org.example.asbe.mapper.CategoryMapper;
import org.example.asbe.model.dto.CategoryDto;
import org.example.asbe.model.entity.Category;
import org.example.asbe.repository.CategoryRepository;
import org.example.asbe.service.CategoryService;
import org.example.asbe.util.CustomException;
import org.example.asbe.util.MessageUtil;
import org.example.asbe.util.dto.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public PagedResponse<CategoryDto> listCategory(int page, int size) {
        Pageable categoryable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "id");
        Page<Category> entities = repository.findAll(categoryable);

        return new PagedResponse<>(
                categoryMapper.toDtoList(entities.getContent()),
                page,
                size,
                entities.getTotalElements(),
                entities.getTotalPages(),
                entities.isLast(),
                entities.getSort().toString()
        );
    }

    @Override
    public PagedResponse<CategoryDto> filterListCategory(String name) {
//        List<Category> entities = repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<Category> entities;

        if (name != null && !name.trim().isEmpty()) {
            // Tìm kiếm theo name
            entities = repository.findByNameContainingIgnoreCaseOrderByIdAsc(name);
        } else {
            // Lấy tất cả nếu không có từ khóa
            entities = repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        }

        List<CategoryDto> listCategoryDTOS = new ArrayList<>();

        for (Category entity : entities) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(entity.getId());
            categoryDto.setName(entity.getName());
            categoryDto.setDescription(entity.getDescription());
//            categoryDto.setCreatedAt(entity.getCreatedAt());
//            categoryDto.setUpdatedAt(entity.getUpdatedAt());
            listCategoryDTOS.add(categoryDto);
        }

        return new PagedResponse<>(
                listCategoryDTOS,
                1, // page (bạn có thể set là 1 nếu không dùng phân trang)
                listCategoryDTOS.size(), // size (số phần tử trong list)
                listCategoryDTOS.size(), // totalElements
                1, // totalPages
                true, // isLast (vì không phân trang, luôn là trang cuối)
                Sort.by(Sort.Direction.ASC, "id").toString()
        );
    }

    @Override
    public String addCategory(Category category) {
        LocalDateTime now = LocalDateTime.now();
        category.setCreatedAt(now);
        repository.save(category);
        return messageUtil.getMessage("success");
    }

    @Override
    public Category updateCategory(Category category, Integer id) {
        if(repository.existsById(Long.valueOf(id))){
            LocalDateTime now = LocalDateTime.now();
            Category existingAuthor = repository.findById(Long.valueOf(id)).get();
            existingAuthor.setName(category.getName());
            existingAuthor.setDescription(category.getDescription());
            existingAuthor.setParent(category.getParent());
            existingAuthor.setUpdatedAt(now);
            return repository.save(existingAuthor);
        }
        return  null;
    }

    @Override
    public String deleteCategory(Integer id) {
        if (!repository.existsById(Long.valueOf(id))) {
            throw new CustomException(messageUtil.getMessage("error.category.notFound", id));
        }
        repository.deleteById(Long.valueOf(id));
        return messageUtil.getMessage("success.category.delete", id);
    }
}
