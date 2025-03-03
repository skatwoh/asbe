package org.example.asbe.mapper;

import org.example.asbe.model.dto.CategoryDto;
import org.example.asbe.model.entity.Category;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)public interface CategoryMapper extends EntityMapper<CategoryDto, Category> {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)Category partialUpdate(CategoryDto categoryDto, @MappingTarget Category category);
}