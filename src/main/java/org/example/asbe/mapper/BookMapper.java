package org.example.asbe.mapper;
import org.example.asbe.model.dto.BookDTO;
import org.example.asbe.model.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookMapper extends EntityMapper<BookDTO, Book>{

}
