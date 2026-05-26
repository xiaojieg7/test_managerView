package com.property.entity;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private Integer code;
    private String msg;
    private T data;
    
    public static <T> ApiResponse<T> success() {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMsg("操作成功");
        return response;
    }
    
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMsg("操作成功");
        response.setData(data);
        return response;
    }
    
    public static <T> ApiResponse<T> success(String msg, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }
    
    public static <T> ApiResponse<T> error(String msg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(500);
        response.setMsg(msg);
        return response;
    }
    
    public static <T> ApiResponse<T> error(Integer code, String msg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }
    
    public static <T> ApiResponse<T> unauthorized(String msg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(401);
        response.setMsg(msg);
        return response;
    }
    
    public static <T> ApiResponse<T> forbidden(String msg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(403);
        response.setMsg(msg);
        return response;
    }
}
