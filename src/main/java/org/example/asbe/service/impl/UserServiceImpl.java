package org.example.asbe.service.impl;

import org.example.asbe.mapper.UserMapper;
import org.example.asbe.model.dto.UserDTO;
import org.example.asbe.model.entity.UserInfo;
import org.example.asbe.repository.UserInfoRepository;
import org.example.asbe.service.UserService;
import org.example.asbe.util.dto.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private UserMapper userMapper;


    @Override
    public PagedResponse<UserDTO> listUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "id");
        Page<UserInfo> entities = repository.findAll(pageable);

        return new PagedResponse<>(
                userMapper.toDtoList(entities.getContent()),
                page,
                size,
                entities.getTotalElements(),
                entities.getTotalPages(),
                entities.isLast(),
                entities.getSort().toString()
        );
    }

    @Override
    public UserInfo updateUser(UserInfo userInfo, Integer id) {
        if (repository.existsById(id)) {

            UserInfo existingUserInfo = repository.findById(id).get();
            existingUserInfo.setEmail(userInfo.getEmail());
            existingUserInfo.setUsername(userInfo.getUsername());
            return repository.save(userInfo);
        }
        return null;
    }


}
