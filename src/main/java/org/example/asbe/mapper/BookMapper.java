package org.example.asbe.mapper;
import org.example.asbe.model.dto.BookDTO;
import org.example.asbe.model.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper extends EntityMapper<BookDTO, Book>{

}
