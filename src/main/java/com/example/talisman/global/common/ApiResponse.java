package com.example.talisman.global.common;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;

    private ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 성공응답 : 데이터가 있는 경우
    public static <T> ApiResponse<T> success(String status, String message, T data) {
        return new ApiResponse<>("SUCCESS", message, data);
    }

    // 성공응답 : 데이터가 없거나 메시지만 전달하는 경우
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("SUCCESS", message, null);
    }

    // 실패응답
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("ERROR", message, null);
    }
}
