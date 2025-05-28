package org.example.asbe.model.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.Instant;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String role;
    private LocalDateTime createdAt;
    private Instant updatedAt;
}
