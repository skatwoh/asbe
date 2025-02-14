package org.example.asbe.service;

import org.example.asbe.mapper.UserMapper;
import org.example.asbe.model.dto.UserDTO;
import org.example.asbe.model.entity.UserInfo;
import org.example.asbe.repository.UserInfoRepository;
import org.example.asbe.util.CustomException;
import org.example.asbe.util.MessageUtil;
import org.example.asbe.util.dto.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByUsername(username); // Assuming 'email' is used as username

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public String addUser(UserInfo userInfo) {
        if(repository.findByUsername(userInfo.getUsername()).isPresent()) {
            throw new CustomException(messageUtil.getMessage("error.user.exists", userInfo.getUsername()));
        } else if(repository.findByEmail(userInfo.getEmail()).isPresent()) {
            throw new CustomException(messageUtil.getMessage("error.email.exists", userInfo.getEmail()));
        }
        // Encode password before saving the user
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return messageUtil.getMessage("success.user.added", userInfo.getUsername());
    }

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

}

