package com.example.talisman.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public enum ErrorCode {
    INVALID_DATA("INVALID_DATA", "잘못된 데이터 요청", HttpStatus.BAD_REQUEST),
    OPEN_AI_ERROR("OPEN_AI_ERROR", "외부 AI API 연동 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "기타 서버 내부 에러", HttpStatus.INTERNAL_SERVER_ERROR);

    private HttpStatus status;
    private String code;
    private String message;

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
