package com.property.controller;

import com.property.entity.*;
import com.property.security.UserDetailsImpl;
import com.property.service.OwnerService;
import com.property.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repair")
public class RepairController {

    @Autowired
    private RepairService repairService;
    
    @Autowired
    private OwnerService ownerService;

    @GetMapping
    public ApiResponse<PageResult<Repair>> getRepairPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        String role = userDetails.getUser().getRole();
        Long ownerId = null;
        Long repairerId = null;
        
        if ("OWNER".equals(role)) {
            Owner owner = ownerService.getOwnerByUserId(userDetails.getUser().getId());
            if (owner != null) {
                ownerId = owner.getId();
            }
        } else if ("REPAIRER".equals(role)) {
            repairerId = userDetails.getUser().getId();
        }
        
        PageResult<Repair> result = repairService.getRepairPage(pageNum, pageSize, status, keyword, ownerId, repairerId);
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}")
    public ApiResponse<Repair> getRepairById(@PathVariable Long id) {
        Repair repair = repairService.getRepairById(id);
        return ApiResponse.success(repair);
    }

    @PostMapping
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ApiResponse<Repair> createRepair(@RequestBody Repair repair,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if ("OWNER".equals(userDetails.getUser().getRole())) {
            Owner owner = ownerService.getOwnerByUserId(userDetails.getUser().getId());
            if (owner != null) {
                repair.setOwnerId(owner.getId());
            }
        }
        Repair result = repairService.createRepair(repair);
        return ApiResponse.success("提交成功", result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Repair> updateRepair(@PathVariable Long id, @RequestBody Repair repair) {
        repair.setId(id);
        Repair result = repairService.updateRepair(repair);
        return ApiResponse.success("更新成功", result);
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Repair> assignRepairer(@PathVariable Long id, @RequestParam Long repairerId) {
        Repair result = repairService.assignRepairer(id, repairerId);
        return ApiResponse.success("分配成功", result);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('REPAIRER') or hasRole('ADMIN')")
    public ApiResponse<Repair> updateStatus(@PathVariable Long id,
                                             @RequestParam String status,
                                             @RequestParam(required = false) String remark,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Repair result = repairService.updateRepairStatus(id, status, remark);
        return ApiResponse.success("状态更新成功", result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteRepair(@PathVariable Long id) {
        repairService.deleteRepair(id);
        return ApiResponse.success("删除成功", null);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('OWNER')")
    public ApiResponse<List<Repair>> getMyRepairs(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Owner owner = ownerService.getOwnerByUserId(userDetails.getUser().getId());
        if (owner == null) {
            return ApiResponse.error("未找到业主信息");
        }
        List<Repair> repairs = repairService.getMyRepairs(owner.getId());
        return ApiResponse.success(repairs);
    }

    @GetMapping("/assigned")
    @PreAuthorize("hasRole('REPAIRER') or hasRole('ADMIN')")
    public ApiResponse<List<Repair>> getAssignedRepairs(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Repair> repairs = repairService.getAssignedRepairs(userDetails.getUser().getId());
        return ApiResponse.success(repairs);
    }
}
