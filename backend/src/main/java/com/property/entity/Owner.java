package com.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("owner")
public class Owner {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String idCard;
    
    private String building;
    
    private String unit;
    
    private String roomNumber;
    
    private LocalDate checkInDate;
    
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String realName;
    
    @TableField(exist = false)
    private String phone;
    
    @TableField(exist = false)
    private String email;
}
