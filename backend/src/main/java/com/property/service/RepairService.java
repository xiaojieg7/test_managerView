package com.property.service;

import com.property.entity.Repair;
import com.property.entity.PageResult;
import java.util.List;

public interface RepairService {
    PageResult<Repair> getRepairPage(Integer pageNum, Integer pageSize, String status, String keyword, Long ownerId, Long repairerId);
    Repair getRepairById(Long id);
    Repair createRepair(Repair repair);
    Repair updateRepair(Repair repair);
    Repair assignRepairer(Long repairId, Long repairerId);
    Repair updateRepairStatus(Long repairId, String status, String remark);
    void deleteRepair(Long id);
    List<Repair> getMyRepairs(Long ownerId);
    List<Repair> getAssignedRepairs(Long repairerId);
}
