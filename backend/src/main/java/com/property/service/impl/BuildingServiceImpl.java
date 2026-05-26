package com.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.property.entity.Building;
import com.property.mapper.BuildingMapper;
import com.property.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingMapper buildingMapper;

    @Override
    public List<Building> getAllBuildings() {
        return buildingMapper.selectList(new LambdaQueryWrapper<Building>()
                .eq(Building::getStatus, 1)
                .orderByAsc(Building::getBuildingCode));
    }

    @Override
    public Building getBuildingById(Long id) {
        return buildingMapper.selectById(id);
    }

    @Override
    @Transactional
    public Building createBuilding(Building building) {
        building.setStatus(1);
        buildingMapper.insert(building);
        return building;
    }

    @Override
    @Transactional
    public Building updateBuilding(Building building) {
        buildingMapper.updateById(building);
        return building;
    }

    @Override
    @Transactional
    public void deleteBuilding(Long id) {
        buildingMapper.deleteById(id);
    }
}
