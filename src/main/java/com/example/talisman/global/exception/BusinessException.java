package com.example.talisman.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class BusinessException extends RuntimeException {
    private String code;
    private HttpStatus status;

    // ErrorCode Enum을 직접 전달받는 편리한 생성
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.status = errorCode.getStatus();
    }

    // 커스텀 메시지가 필요한 경우
    public BusinessException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.code = errorCode.getCode();
        this.status = errorCode.getStatus();
    }
}
