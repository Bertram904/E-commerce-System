package com.fpt.ecommerce.exception;

import com.fpt.ecommerce.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandling {

    private final MessageSource messageSource;

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<?>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        String message = messageSource.getMessage(
                errorCode.getMessageKey(),
                exception.getArgs(),
                LocaleContextHolder.getLocale()
        );

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(message)
                .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<?>> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            log.warn("Validation error key not found in Enum: {}", enumKey);
        }

        String message = messageSource.getMessage(
                errorCode.getMessageKey(),
                null,
                LocaleContextHolder.getLocale()
        );

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(message)
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<?>> handlingRuntimeException(Exception exception) {
        log.error("Uncategorized error: ", exception);

        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;

        String message = messageSource.getMessage(
                errorCode.getMessageKey(),
                null,
                LocaleContextHolder.getLocale()
        );

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(message)
                .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }
}