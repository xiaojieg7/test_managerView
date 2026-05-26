package com.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.Owner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OwnerMapper extends BaseMapper<Owner> {
    IPage<Owner> selectOwnerPage(Page<Owner> page, @Param("building") String building, @Param("keyword") String keyword);
}
