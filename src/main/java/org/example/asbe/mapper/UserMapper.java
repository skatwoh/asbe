package org.example.asbe.mapper;

import org.example.asbe.model.dto.UserDTO;
import org.example.asbe.model.entity.Userinfo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, Userinfo> {

}
