package org.example.asbe.mapper;

import org.apache.catalina.User;
import org.example.asbe.model.dto.UserDTO;
import org.example.asbe.model.entity.UserInfo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, UserInfo> {
}
