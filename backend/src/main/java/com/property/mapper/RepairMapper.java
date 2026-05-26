package com.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.Repair;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RepairMapper extends BaseMapper<Repair> {
    IPage<Repair> selectRepairPage(Page<Repair> page, @Param("status") String status, @Param("keyword") String keyword, @Param("ownerId") Long ownerId, @Param("repairerId") Long repairerId);
}
