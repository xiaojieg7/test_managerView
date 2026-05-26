package com.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.Owner;
import com.property.entity.PageResult;
import com.property.mapper.OwnerMapper;
import com.property.mapper.UserMapper;
import com.property.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private OwnerMapper ownerMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult<Owner> getOwnerPage(Integer pageNum, Integer pageSize, String building, String keyword) {
        Page<Owner> page = new Page<>(pageNum, pageSize);
        IPage<Owner> result = ownerMapper.selectOwnerPage(page, building, keyword);
        return new PageResult<>(result.getTotal(), result.getRecords());
    }

    @Override
    public Owner getOwnerById(Long id) {
        Owner owner = ownerMapper.selectById(id);
        if (owner != null) {
            enrichOwnerInfo(owner);
        }
        return owner;
    }

    @Override
    public Owner getOwnerByUserId(Long userId) {
        Owner owner = ownerMapper.selectOne(new LambdaQueryWrapper<Owner>()
                .eq(Owner::getUserId, userId));
        if (owner != null) {
            enrichOwnerInfo(owner);
        }
        return owner;
    }

    @Override
    @Transactional
    public Owner createOwner(Owner owner) {
        ownerMapper.insert(owner);
        return owner;
    }

    @Override
    @Transactional
    public Owner updateOwner(Owner owner) {
        ownerMapper.updateById(owner);
        return owner;
    }

    @Override
    @Transactional
    public void deleteOwner(Long id) {
        Owner owner = ownerMapper.selectById(id);
        if (owner != null) {
            userMapper.deleteById(owner.getUserId());
        }
    }

    @Override
    public List<Owner> getAllOwners() {
        return ownerMapper.selectList(new LambdaQueryWrapper<Owner>()
                .eq(Owner::getStatus, 1)
                .orderByAsc(Owner::getBuilding, Owner::getUnit, Owner::getRoomNumber));
    }
    
    private void enrichOwnerInfo(Owner owner) {
        if (owner.getUserId() != null) {
            com.property.entity.User user = userMapper.selectById(owner.getUserId());
            if (user != null) {
                owner.setUsername(user.getUsername());
                owner.setRealName(user.getRealName());
                owner.setPhone(user.getPhone());
                owner.setEmail(user.getEmail());
            }
        }
    }
}
