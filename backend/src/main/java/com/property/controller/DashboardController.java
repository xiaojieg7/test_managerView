package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.property.entity.ApiResponse;
import com.property.entity.PageResult;
import com.property.entity.Repair;
import com.property.entity.User;
import com.property.mapper.UserMapper;
import com.property.service.PaymentService;
import com.property.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {

    @Autowired
    private RepairService repairService;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalUsers", userMapper.selectCount(null));
        stats.put("totalOwners", userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getRole, "OWNER")));
        stats.put("totalRepairers", userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getRole, "REPAIRER")));
        
        PageResult<Repair> pendingRepairs = repairService.getRepairPage(1, 1, "PENDING", null, null, null);
        stats.put("pendingRepairs", pendingRepairs.getTotal());
        
        PageResult<Repair> allRepairs = repairService.getRepairPage(1, 1, null, null, null, null);
        stats.put("totalRepairs", allRepairs.getTotal());
        
        return ApiResponse.success(stats);
    }
}
