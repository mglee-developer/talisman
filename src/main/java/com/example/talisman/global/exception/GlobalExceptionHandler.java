package com.example.talisman.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
   @ExceptionHandler(BusinessException.class)
   public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
      log.warn("BusinessException: {} - {}", e.getCode(), e.getMessage());
      return ResponseEntity
              .status(e.getStatus())
              .body(new ErrorResponse(e.getCode(), e.getMessage()));
   }

   // Validation 예외 처리 (@Valid 실패)
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ErrorResponse> handleValidationException(
           MethodArgumentNotValidException e) {
      String message = e.getBindingResult()
              .getFieldErrors()
              .get(0)
              .getDefaultMessage();
      log.warn("Validation Exception : {}", message);
      return ResponseEntity
              .badRequest()
              .body(new ErrorResponse(ErrorCode.INVALID_DATA.getCode(), message));
   }

   // 그 외 예외 처리
   @ExceptionHandler(Exception.class)
   public ResponseEntity<ErrorResponse> handleException(Exception e) {
      log.warn("Unhandled Internal Server Error: {}", e);
      return ResponseEntity
              .internalServerError()
              .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
   }
}
