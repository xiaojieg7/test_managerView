package com.property.service;

import com.property.entity.LoginRequest;
import com.property.entity.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    void logout();
}
