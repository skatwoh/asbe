package org.example.asbe.mapper;

import org.example.asbe.model.dto.AuthorDto;
import org.example.asbe.model.entity.Author;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.ERROR, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AuthorMapper extends EntityMapper<AuthorDto, Author> {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Author partialUpdate(AuthorDto authorDto, @MappingTarget Author author);
}