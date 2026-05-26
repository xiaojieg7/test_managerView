package com.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.property.entity.*;
import com.property.mapper.UserMapper;
import com.property.mapper.OwnerMapper;
import com.property.service.AuthService;
import com.property.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private OwnerMapper ownerMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername()));
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (user.getStatus() != 1) {
            throw new RuntimeException("账号已被禁用");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        Long ownerId = null;
        if ("OWNER".equals(user.getRole())) {
            Owner owner = ownerMapper.selectOne(new LambdaQueryWrapper<Owner>()
                    .eq(Owner::getUserId, user.getId()));
            if (owner != null) {
                ownerId = owner.getId();
            }
        }
        
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole());
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setRole(user.getRole());
        response.setUserId(user.getId());
        response.setOwnerId(ownerId);
        response.setPermissions(getPermissionsByRole(user.getRole()));
        
        return response;
    }

    @Override
    public void logout() {
    }

    private List<String> getPermissionsByRole(String role) {
        List<String> permissions = new ArrayList<>();
        switch (role) {
            case "ADMIN":
                permissions.add("user:manage");
                permissions.add("owner:manage");
                permissions.add("repair:manage");
                permissions.add("repair:assign");
                permissions.add("payment:manage");
                permissions.add("notice:manage");
                permissions.add("building:manage");
                permissions.add("dashboard:view");
                break;
            case "REPAIRER":
                permissions.add("repair:view");
                permissions.add("repair:update");
                break;
            case "OWNER":
                permissions.add("repair:submit");
                permissions.add("repair:view");
                permissions.add("payment:view");
                permissions.add("payment:pay");
                permissions.add("notice:view");
                break;
        }
        return permissions;
    }
}
