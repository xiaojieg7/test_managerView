package com.property.controller;

import com.property.entity.ApiResponse;
import com.property.entity.Building;
import com.property.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/building")
@PreAuthorize("hasRole('ADMIN')")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @GetMapping("/all")
    public ApiResponse<List<Building>> getAllBuildings() {
        List<Building> buildings = buildingService.getAllBuildings();
        return ApiResponse.success(buildings);
    }

    @GetMapping("/{id}")
    public ApiResponse<Building> getBuildingById(@PathVariable Long id) {
        Building building = buildingService.getBuildingById(id);
        return ApiResponse.success(building);
    }

    @PostMapping
    public ApiResponse<Building> createBuilding(@RequestBody Building building) {
        Building result = buildingService.createBuilding(building);
        return ApiResponse.success("创建成功", result);
    }

    @PutMapping("/{id}")
    public ApiResponse<Building> updateBuilding(@PathVariable Long id, @RequestBody Building building) {
        building.setId(id);
        Building result = buildingService.updateBuilding(building);
        return ApiResponse.success("更新成功", result);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBuilding(@PathVariable Long id) {
        buildingService.deleteBuilding(id);
        return ApiResponse.success("删除成功", null);
    }
}
