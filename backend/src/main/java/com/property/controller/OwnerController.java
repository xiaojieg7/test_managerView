package com.property.controller;

import com.property.entity.*;
import com.property.mapper.OwnerMapper;
import com.property.service.OwnerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

@RestController
@RequestMapping("/api/admin/owners")
@PreAuthorize("hasRole('ADMIN')")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;
    
    @Autowired
    private OwnerMapper ownerMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ApiResponse<PageResult<Owner>> getOwnerPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String building,
            @RequestParam(required = false) String keyword) {
        PageResult<Owner> result = ownerService.getOwnerPage(pageNum, pageSize, building, keyword);
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}")
    public ApiResponse<Owner> getOwnerById(@PathVariable Long id) {
        Owner owner = ownerService.getOwnerById(id);
        return ApiResponse.success(owner);
    }

    @GetMapping("/all")
    public ApiResponse<List<Owner>> getAllOwners() {
        List<Owner> owners = ownerService.getAllOwners();
        return ApiResponse.success(owners);
    }

    @PostMapping
    public ApiResponse<Owner> createOwner(@RequestBody Owner owner) {
        Owner result = ownerService.createOwner(owner);
        return ApiResponse.success("创建成功", result);
    }

    @PutMapping("/{id}")
    public ApiResponse<Owner> updateOwner(@PathVariable Long id, @RequestBody Owner owner) {
        owner.setId(id);
        Owner result = ownerService.updateOwner(owner);
        return ApiResponse.success("更新成功", result);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
        return ApiResponse.success("删除成功", null);
    }
}
