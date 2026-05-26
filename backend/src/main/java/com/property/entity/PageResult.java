package com.property.entity;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private Long total;
    private List<T> records;
    private Long current;
    private Long size;
    private Long pages;
    
    public PageResult() {}
    
    public PageResult(Long total, List<T> records) {
        this.total = total;
        this.records = records;
    }
}
