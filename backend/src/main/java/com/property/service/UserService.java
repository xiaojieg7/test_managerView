package com.property.service;

import com.property.entity.User;

public interface UserService {
    User getUserById(Long id);
    User getUserByUsername(String username);
}
