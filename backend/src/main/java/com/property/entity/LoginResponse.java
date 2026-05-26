package com.property.entity;

import lombok.Data;
import java.util.List;

@Data
public class LoginResponse {
    private String token;
    private String username;
    private String realName;
    private String role;
    private Long userId;
    private Long ownerId;
    private List<String> permissions;
}
