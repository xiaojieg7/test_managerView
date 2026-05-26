package com.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("repair")
public class Repair {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long ownerId;
    
    private Long repairerId;
    
    private String title;
    
    private String description;
    
    private String category;
    
    private String status;
    
    private String priority;
    
    private String repairRemark;
    
    private LocalDateTime completeTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableField(exist = false)
    private String ownerName;
    
    @TableField(exist = false)
    private String ownerBuilding;
    
    @TableField(exist = false)
    private String ownerUnit;
    
    @TableField(exist = false)
    private String ownerRoomNumber;
    
    @TableField(exist = false)
    private String repairerName;
}
