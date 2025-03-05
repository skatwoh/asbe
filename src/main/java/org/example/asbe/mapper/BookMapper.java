package org.example.asbe.mapper;

import org.example.asbe.model.dto.BookDTO;
import org.example.asbe.model.entity.Book;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper extends EntityMapper<BookDTO, Book> {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Book partialUpdate(BookDTO bookDTO, @MappingTarget Book book);
}