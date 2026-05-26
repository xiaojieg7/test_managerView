package com.property.service;

import com.property.entity.Building;
import java.util.List;

public interface BuildingService {
    List<Building> getAllBuildings();
    Building getBuildingById(Long id);
    Building createBuilding(Building building);
    Building updateBuilding(Building building);
    void deleteBuilding(Long id);
}
