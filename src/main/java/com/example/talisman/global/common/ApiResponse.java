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

    // 데이터가 있는 성공 응답 (data를 정상 전달)
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("SUCCESS", message, data);
    }

    // 데이터가 없는 성공 응답 (메시지만 전달)
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>("SUCCESS", message, null);
    }

    // 실패 응답
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("ERROR", message, null);
    }
}