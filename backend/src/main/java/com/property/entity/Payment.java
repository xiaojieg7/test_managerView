package com.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("payment")
public class Payment {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long ownerId;
    
    private String paymentType;
    
    private BigDecimal amount;
    
    private String period;
    
    private String status;
    
    private LocalDate dueDate;
    
    private LocalDateTime paidTime;
    
    private String paymentMethod;
    
    private String remark;
    
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
}
