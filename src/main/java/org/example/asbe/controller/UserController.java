package org.example.asbe.controller;

import org.example.asbe.model.entity.AuthRequest;
import org.example.asbe.model.entity.Userinfo;
import org.example.asbe.service.JwtService;
import org.example.asbe.service.UserInfoService;
import org.example.asbe.service.impl.UserServiceImpl;
import org.example.asbe.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<?> addNewUser(@RequestBody Userinfo userInfo) {
        return ResponseUtil.response(HttpStatus.OK, service.addUser(userInfo), null, null);
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            List<String> roles = null;

            if(service.getUserByUsername(authRequest.getUsername()).getRole().equals("ROLE_ADMIN")) {
                roles = Collections.singletonList("ROLE_ADMIN");
            } else {
                roles = Collections.singletonList("ROLE_USER");
            }

            String email = service.getUserByUsername(authRequest.getUsername()).getEmail();

            // Nếu authenticate thành công, tiếp tục tạo token
            String token = jwtService.generateToken(authRequest.getUsername(), email, roles);
            return ResponseUtil.success(token, "Token generated successfully!");

        } catch (BadCredentialsException ex) {
            // Xử lý lỗi đăng nhập sai thông tin
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password!", ex);
        } catch (UsernameNotFoundException ex) {
            // Xử lý lỗi user không tồn tại
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!", ex);
        }

    }

    @GetMapping("/list-user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> listUser(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseUtil.success(service.listUsers(page, size), "List user successfully!");
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<?> updateUser(@RequestBody Userinfo userInfo, @PathVariable Integer id) {
        return ResponseUtil.success(userServiceImpl.updateUser(userInfo, id), "Update user successfully!");
    }

}

