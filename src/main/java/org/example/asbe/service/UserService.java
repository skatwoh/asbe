package org.example.asbe.service;

import org.example.asbe.model.dto.UserDTO;
import org.example.asbe.model.entity.Userinfo;
import org.example.asbe.util.dto.PagedResponse;

public interface UserService {

    PagedResponse<UserDTO> listUsers(int page, int size);

    Userinfo updateUser(Userinfo userInfo, Integer id);
}
