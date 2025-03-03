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
        List<CategoryDto> listCategoryDTOS =  categoryMapper.toDtoList(entities.getContent());

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
    public String addCategory(Category category) {
        repository.save(category);
        return messageUtil.getMessage("success");
    }

    @Override
    public Category updateCategory(Category category, Integer id) {
        if(repository.existsById(id)){
            Category existingAuthor = repository.findById(id).get();
            existingAuthor.setName(category.getName());
            existingAuthor.setDescription(category.getDescription());
            existingAuthor.setParent(category.getParent());
            return repository.save(category);
        }
        return  null;
    }

    @Override
    public String deleteCategory(Integer id) {
        if (!repository.existsById(id)) {
            throw new CustomException(messageUtil.getMessage("error.category.notFound", id));
        }
        repository.deleteById(id);
        return messageUtil.getMessage("success.category.delete", id);
    }
}
