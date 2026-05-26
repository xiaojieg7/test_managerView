package com.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.Repair;
import com.property.entity.PageResult;
import com.property.mapper.RepairMapper;
import com.property.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RepairServiceImpl implements RepairService {

    @Autowired
    private RepairMapper repairMapper;

    @Override
    public PageResult<Repair> getRepairPage(Integer pageNum, Integer pageSize, String status, String keyword, Long ownerId, Long repairerId) {
        Page<Repair> page = new Page<>(pageNum, pageSize);
        IPage<Repair> result = repairMapper.selectRepairPage(page, status, keyword, ownerId, repairerId);
        return new PageResult<>(result.getTotal(), result.getRecords());
    }

    @Override
    public Repair getRepairById(Long id) {
        return repairMapper.selectById(id);
    }

    @Override
    @Transactional
    public Repair createRepair(Repair repair) {
        repair.setStatus("PENDING");
        repair.setPriority(repair.getPriority() == null ? "NORMAL" : repair.getPriority());
        repairMapper.insert(repair);
        return repair;
    }

    @Override
    @Transactional
    public Repair updateRepair(Repair repair) {
        repairMapper.updateById(repair);
        return repair;
    }

    @Override
    @Transactional
    public Repair assignRepairer(Long repairId, Long repairerId) {
        Repair repair = repairMapper.selectById(repairId);
        if (repair == null) {
            throw new RuntimeException("报修记录不存在");
        }
        repair.setRepairerId(repairerId);
        if ("PENDING".equals(repair.getStatus())) {
            repair.setStatus("PROCESSING");
        }
        repairMapper.updateById(repair);
        return repair;
    }

    @Override
    @Transactional
    public Repair updateRepairStatus(Long repairId, String status, String remark) {
        Repair repair = repairMapper.selectById(repairId);
        if (repair == null) {
            throw new RuntimeException("报修记录不存在");
        }
        repair.setStatus(status);
        if (remark != null) {
            repair.setRepairRemark(remark);
        }
        if ("COMPLETED".equals(status)) {
            repair.setCompleteTime(LocalDateTime.now());
        }
        repairMapper.updateById(repair);
        return repair;
    }

    @Override
    @Transactional
    public void deleteRepair(Long id) {
        repairMapper.deleteById(id);
    }

    @Override
    public List<Repair> getMyRepairs(Long ownerId) {
        return repairMapper.selectList(new LambdaQueryWrapper<Repair>()
                .eq(Repair::getOwnerId, ownerId)
                .orderByDesc(Repair::getCreateTime));
    }

    @Override
    public List<Repair> getAssignedRepairs(Long repairerId) {
        return repairMapper.selectList(new LambdaQueryWrapper<Repair>()
                .eq(Repair::getRepairerId, repairerId)
                .orderByDesc(Repair::getCreateTime));
    }
}
