package com.property.service;

import com.property.entity.Owner;
import com.property.entity.PageResult;
import java.util.List;

public interface OwnerService {
    PageResult<Owner> getOwnerPage(Integer pageNum, Integer pageSize, String building, String keyword);
    Owner getOwnerById(Long id);
    Owner getOwnerByUserId(Long userId);
    Owner createOwner(Owner owner);
    Owner updateOwner(Owner owner);
    void deleteOwner(Long id);
    List<Owner> getAllOwners();
}
