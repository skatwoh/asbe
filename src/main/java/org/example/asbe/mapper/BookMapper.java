package org.example.asbe.mapper;
import org.example.asbe.model.dto.BookDTO;
import org.example.asbe.model.entity.Book;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface BookMapper extends EntityMapper<BookDTO, Book>{
    BookDTO toDto(Book entity);

    Book toEntity(BookDTO dto);

    List<BookDTO> toDtoList(List<Book> entityList);
}
